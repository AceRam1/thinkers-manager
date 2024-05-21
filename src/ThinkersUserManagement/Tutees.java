package ThinkersUserManagement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tutees {
    
    private int tuteeId;
    private String name;
    private String nickname;
    private String gender;
    private int age;
    private String address;
    private String contactInformation;
    private String subjectTaking;
    private String timeSlot;

    private final StringProperty nameProperty;
    private final SimpleIntegerProperty tuteeIdProperty;
    private final StringProperty nicknameProperty;
    private final StringProperty genderProperty;
    private final SimpleIntegerProperty ageProperty;
    private final StringProperty addressProperty;
    private final StringProperty contactInformationProperty;
    private final StringProperty subjectTakingProperty;
    private final StringProperty timeSlotProperty;

    // Constructor for data retrieval (assuming this is the main use case)
public Tutees(int tuteeId, String name, String nickname, String gender, int age, String address, String contactInformation, String subjectTaking, String timeSlot) {
        this.tuteeId = tuteeId;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.contactInformation = contactInformation;
        this.subjectTaking = subjectTaking;
        this.timeSlot = timeSlot;

        this.nameProperty = new SimpleStringProperty(name);
        this.tuteeIdProperty = new SimpleIntegerProperty(tuteeId);
        this.nicknameProperty = new SimpleStringProperty(nickname);
        this.genderProperty = new SimpleStringProperty(gender);
        this.ageProperty = new SimpleIntegerProperty(age);
        this.addressProperty = new SimpleStringProperty(address);
        this.contactInformationProperty = new SimpleStringProperty(contactInformation);
        this.subjectTakingProperty = new SimpleStringProperty(subjectTaking);
        this.timeSlotProperty = new SimpleStringProperty(timeSlot);
    }

    // Constructor for TableView display
    public Tutees(String name) {
        this.name = name;
        this.nameProperty = new SimpleStringProperty(name);
        this.tuteeIdProperty = new SimpleIntegerProperty(0);
        this.nicknameProperty = new SimpleStringProperty("");
        this.genderProperty = new SimpleStringProperty("");
        this.ageProperty = new SimpleIntegerProperty(0);
        this.addressProperty = new SimpleStringProperty("");
        this.contactInformationProperty = new SimpleStringProperty("");
        this.subjectTakingProperty = new SimpleStringProperty("");
        this.timeSlotProperty = new SimpleStringProperty("");
    }

    public Tutees(int tuteeId, String timeSlot) {
        this.tuteeId = tuteeId;
        this.nameProperty = new SimpleStringProperty("");
        this.tuteeIdProperty = new SimpleIntegerProperty(tuteeId);
        this.nicknameProperty = new SimpleStringProperty("");
        this.genderProperty = new SimpleStringProperty("");
        this.ageProperty = new SimpleIntegerProperty(0);
        this.addressProperty = new SimpleStringProperty("");
        this.contactInformationProperty = new SimpleStringProperty("");
        this.subjectTakingProperty = new SimpleStringProperty("");
        this.timeSlotProperty = new SimpleStringProperty(timeSlot);
    }

    public int getTuteeId() {
        return tuteeId;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public String getSubjectTaking() {
        return subjectTaking;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public SimpleIntegerProperty tuteeIdProperty() {
        return tuteeIdProperty;
    }

    public StringProperty nicknameProperty() {
        return nicknameProperty;
    }

    public StringProperty genderProperty() {
        return genderProperty;
    }

    public SimpleIntegerProperty ageProperty() {
        return ageProperty;
    }

    public StringProperty addressProperty() {
        return addressProperty;
    }

    public StringProperty contactInformationProperty() {
        return contactInformationProperty;
    }

    public StringProperty subjectTakingProperty() {
        return subjectTakingProperty;
    }

    public StringProperty timeSlotProperty() {
        return timeSlotProperty;
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public void setName(String name) {
        this.name = name;
        nameProperty.set(name);
    }

    @Override
public String toString() {
    return "Tutees{" +
            "tuteeId=" + tuteeId +
            ", name='" + name + '\'' +
            ", nickname='" + nickname + '\'' +
            ", gender='" + gender + '\'' +
            ", age=" + age +
            ", address='" + address + '\'' +
            ", contactInformation='" + contactInformation + '\'' +
            ", subjectTaking='" + subjectTaking + '\'' +
            ", timeSlot='" + timeSlot + '\'' +
            '}';
}
}