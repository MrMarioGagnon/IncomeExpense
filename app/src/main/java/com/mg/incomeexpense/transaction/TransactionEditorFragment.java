package com.mg.incomeexpense.transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.DatePickerFragment;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;
import com.mg.incomeexpense.extension.ExtensionDataExtractor;
import com.mg.incomeexpense.extension.ExtensionFragmentFactory;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionEditorFragment extends FragmentBase implements DatePickerDialog.OnDateSetListener {

    private Transaction mTransaction = null;
    private TransactionValidator mObjectValidator;

    private TextView mTextViewAccountName;

    private Spinner mSpinnerPaymentMethod;
    private ArrayAdapter<PaymentMethod> mPaymentMethodSpinnerAdapter;

    private Spinner mSpinnerCategory;

    private TextView mTextViewCurrency;
    private TextView mTextViewExchangeRate;

    private TextView mTextViewDate;

    private EditText mEditTextAmount;

    private ImageView mImageViewDate;

    private List<PaymentMethod> mPaymentMethods;
    private List<Category> mCategories;

    private ImageView mImageViewContributors;
    private View.OnClickListener mOnContributorImageButtonClickListener;
    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
    private TextView mTextViewContributors;
    private List<Contributor> mAvailableContributors;
    private List<Contributor> mSelectedContributors;

    private View mFrameExtension;
    private String mExtensionData;

    private View mRootEditorView;

    private boolean[] mCheckedContributor;

    public TransactionEditorFragment() {

        mSelectedContributors = new ArrayList<>();
        mOnContributorImageButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContributorSetterDialog();
            }
        };

        mContributorMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {
                mSelectedContributors.clear();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < idSelected.length; i++) {
                    if (idSelected[i]) {
                        Contributor contributor = mAvailableContributors.get(i);
                        mSelectedContributors.add(contributor);
                        sb.append(String.format("%1$s%2$s", (sb.length() == 0 ? "" : ","), contributor.getName()));
                    }
                }
                mTextViewContributors.setText(sb.toString());
            }
        };

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mTransaction = (Transaction) bundle.getSerializable("item");
        Objects.requireNonNull(mTransaction, "An transaction is mandatory");
        Objects.requireNonNull(mTransaction.getAccount(), "An account is mandatory");
        Objects.requireNonNull(mTransaction.getAccount().getCategories(), "A list of categories is mandatory");
        Objects.requireNonNull(mTransaction.getAccount().getContributors(), "A list of contributors is mandatory");
        Objects.requireNonNull(mTransaction.getAccount().getCategories(), "A list of category is mandatory");

        mPaymentMethods = (ArrayList<PaymentMethod>) bundle.getSerializable("paymentMethods");
        Objects.requireNonNull(mPaymentMethods, "A payment method is mandatory");

        mPaymentMethodSpinnerAdapter = new ArrayAdapter<PaymentMethod>(getActivity(), R.layout.spinner_item, mPaymentMethods);
        mPaymentMethodSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        mCategories = mTransaction.getAccount().getCategories();

        mAvailableContributors = mTransaction.getAccount().getContributors();
        mSelectedContributors.addAll(mTransaction.getContributors());

        mObjectValidator = TransactionValidator.create(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivityBase) getActivity()).getSupportActionBar();
        if (actionBar != null) {

            if (mTransaction.getType().equals(Transaction.TransactionType.Expense)) {
                actionBar.setTitle(getString(mTransaction.isNew() ? R.string.title_transaction_editor_add_expense : R.string.title_transaction_editor_update_expense));
            } else {
                actionBar.setTitle(getString(mTransaction.isNew() ? R.string.title_transaction_editor_add_income : R.string.title_transaction_editor_update_income));
            }

        } else {
            actionBar.setTitle(getString(mTransaction.isNew() ? R.string.title_transaction_editor_add : R.string.title_transaction_editor_update));
        }

        View rootView = inflater.inflate(R.layout.transaction_editor_fragment, container, false);

        mTextViewAccountName = (TextView) rootView.findViewById(R.id.text_view_account_name);

        mSpinnerPaymentMethod = (Spinner) rootView.findViewById(R.id.spinner_payment_method);
        mSpinnerPaymentMethod.setAdapter(mPaymentMethodSpinnerAdapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mSpinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                PaymentMethod paymentMethod = mPaymentMethodSpinnerAdapter.getItem(position);
                Double exchangeRate = paymentMethod.getExchangeRate();
                mTextViewExchangeRate.setText(exchangeRate.toString());

                mTextViewCurrency.setText(paymentMethod.getCurrency());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        mSpinnerCategory = (Spinner) rootView.findViewById(R.id.spinner_category);
        ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(getActivity(), R.layout.spinner_item, mCategories);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinnerCategory.setAdapter(arrayAdapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = mCategories.get(position);

                Bundle bundle = new Bundle();
                Fragment extensionFragment;
                extensionFragment = ExtensionFragmentFactory.create(category.getType());

                bundle.putString("data", mExtensionData);
                extensionFragment.setArguments(bundle);

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                transaction.replace(R.id.frame_layout_extension, extensionFragment).commit();
                mExtensionData = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mTextViewCurrency = (TextView) rootView.findViewById(R.id.text_view_currency);

        mTextViewDate = (TextView) rootView.findViewById(R.id.text_view_date);

        mEditTextAmount = (EditText) rootView.findViewById(R.id.edit_text_amount);
        mTextViewExchangeRate = (TextView) rootView.findViewById(R.id.text_view_exchange_rate);

        mImageViewDate = (ImageView) rootView.findViewById(R.id.image_view_date);
        mImageViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar();
            }
        });

        mImageViewContributors = (ImageView) rootView.findViewById(R.id.image_view_contributors);
        mImageViewContributors.setOnClickListener(mOnContributorImageButtonClickListener);

        mTextViewContributors = (TextView) rootView.findViewById(R.id.text_view_contributors);

        if (mTransaction.isNew()) {
            // get the current date
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            mTransaction.setDate(String.format("%d-%02d-%02d", year, month + 1,
                    day));
            mTransaction.setCurrency(Tools.getDefaultCurrency(getActivity()));
        }

        mTextViewAccountName.setText(mTransaction.getAccount().getName());
        mTextViewDate.setText(mTransaction.getDate());
        mEditTextAmount.setText(mTransaction.getAmount().toString());
        mTextViewExchangeRate.setText(mTransaction.getExchangeRate().toString());
        Tools.setSpinner(mTransaction.getPaymentMethod(), mSpinnerPaymentMethod);
        Tools.setSpinner(mTransaction.getCategory(), mSpinnerCategory);
        mExtensionData = mTransaction.getNote();

        mFrameExtension = rootView.findViewById(R.id.frame_layout_extension);

        if (mTransaction.getAccount().getContributors().size() == 1) {
            mSelectedContributors = mTransaction.getAccount().getContributors();
            mTextViewContributors.setText(mTransaction.getAccount().getContributorsForDisplay());
            mImageViewContributors.setVisibility(View.GONE);
        } else {
            mTextViewContributors.setText(mTransaction.getContributorsForDisplay());
        }

        mRootEditorView = rootView.findViewById(R.id.root_editor_view);

        return rootView;
    }

    private void ShowCalendar() {

        DatePickerFragment picker = new DatePickerFragment();
        picker.setListener(this);


        picker.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        if (mTransaction.isNew()) {
            MenuItem mi = menu.findItem(R.id.action_delete);
            if (null != mi) {
                mi.setVisible(false);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                DialogUtils.twoButtonMessageBox(getContext(), getString(R.string.ask_delete_payment_method), getString(R.string.dialog_title_deleting_payment_method), new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {

                        mTransaction.setDead(true);
                        notifyListener(new ItemStateChangeEvent(mTransaction, false));

                    }
                }, new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {
                        // Do nothing
                    }
                }).show();
                break;
            case R.id.action_save:

                Category category = (Category) mSpinnerCategory.getSelectedItem();
                mTransaction.setCategory(category);

                PaymentMethod paymentMethod = (PaymentMethod) mSpinnerPaymentMethod.getSelectedItem();
                mTransaction.setPaymentMethod(paymentMethod);

                String currency = mTextViewCurrency.getText().toString();
                mTransaction.setCurrency(currency);

                String date = mTextViewDate.getText().toString();
                mTransaction.setDate(date);

                String amount = mEditTextAmount.getText().toString();
                if (amount.trim().length() > 0)
                    mTransaction.setAmount(Double.parseDouble(amount));

                String exchangeRate = mTextViewExchangeRate.getText().toString();
                if (exchangeRate.trim().length() > 0)
                    mTransaction.setExchangeRate(Double.parseDouble(exchangeRate));

                mTransaction.setContributors(mSelectedContributors);

                // Extension
                if (mFrameExtension instanceof ViewGroup) {
                    String note = ExtensionDataExtractor.extract(mFrameExtension);
                    mTransaction.setNote(note);
                }

                ValidationStatus validationStatus = mObjectValidator.Validate(mTransaction);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mTransaction, false));
                } else {
                    displayMessage(mRootEditorView, validationStatus.getMessage());
                }
                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent(mTransaction, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());

        mTextViewDate.setText(formattedDate);

    }

    private void showContributorSetterDialog() {

        try {

            CharSequence[] contributorArray = new CharSequence[mAvailableContributors.size()];
            int i = 0;
            for (Contributor contributor : mAvailableContributors) {
                contributorArray[i++] = contributor.getName();
            }

            Dialog dialog = DialogUtils.childSetterDialog(
                    this.getContext(),
                    contributorArray,
                    mContributorMultipleChoiceEventHandler,
                    getString(R.string.dialog_title_contributor_setter));

            mCheckedContributor = buildContributorsCheckedArray(mAvailableContributors, mSelectedContributors);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ListView lv = ((AlertDialog) dialog).getListView();
                    for (int i = 0; i < mCheckedContributor.length; i++) {
                        lv.setItemChecked(i, mCheckedContributor[i]);
                    }
                }
            });

            dialog.setOwnerActivity(this.getActivity());
            dialog.show();
        } catch (Exception exception) {
            DialogUtils.messageBox(this.getContext(),
                    getString(R.string.error_unable_to_fetch_all_contributor),
                    getString(R.string.dialog_title_contributor_setter)).show();

        }

    }

    private boolean[] buildContributorsCheckedArray(List<Contributor> availableContributors,
                                                    List<Contributor> selectedContributors) {

        boolean[] checked = new boolean[availableContributors.size()];

        for (Contributor contributor : selectedContributors) {
            int index = availableContributors.indexOf(contributor);
            if (index >= 0) {
                checked[index] = true;
            }
        }

        return checked;

    }

}
