package ucf.assignments;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Task {

    private SimpleStringProperty description;
    private SimpleStringProperty dueDate;
    private SimpleBooleanProperty completed;

    public Task(String description, String dueDate) {
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.completed = new SimpleBooleanProperty(false);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String deadline1) {
        description.set(deadline1);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(String dueDate) {
        this.dueDate = new SimpleStringProperty(dueDate);
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }
}
