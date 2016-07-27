package com.mg.incomeexpense.core.dialog;

public interface MultipleChoiceEventHandler {
    MultipleChoiceEventHandler NO_OP = new MultipleChoiceEventHandler() {
        public void execute(boolean[] idSelected) {
        }
    };

    void execute(boolean[] idSelected);
}
