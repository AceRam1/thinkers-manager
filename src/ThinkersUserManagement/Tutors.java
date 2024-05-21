package ThinkersUserManagement;

public class Tutors {
    
    private int tutorID;
    private String firstName;
    private String lastName;
    private String subject;
    private String gender;
    private String address;
    private String contactInformation;
    private String educationalQualification;
    private int teachingExperience;
    private String availability;

    public Tutors(int tutorID, String firstName, String lastName, String subject, String gender, String address, String contactInformation, String educationalQualification, int teachingExperience, String availability) {
        this.tutorID = tutorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.gender = gender;
        this.address = address;
        this.contactInformation = contactInformation;
        this.educationalQualification = educationalQualification;
        this.teachingExperience = teachingExperience;
        this.availability = availability;
    }

    public Tutors(int tutorID, String lastName, String firstName, String subject, String gender) {
        this.tutorID = tutorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.gender = gender;
    }

    public int getTutorID() {
        return tutorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSubject() {
        return subject;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public String getEducationalQualification() {
        return educationalQualification;
    }

    public int getTeachingExperience() {
        return teachingExperience;
    }

    public String getAvailability() {
        return availability;
    }
    
           
    
}
