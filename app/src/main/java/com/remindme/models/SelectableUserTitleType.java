package com.remindme.models;

public class SelectableUserTitleType extends UserType {

    private boolean isSelected = false;

    public SelectableUserTitleType(UserType item, boolean isSelected) {
        super(item.getId(), item.getLabel());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
