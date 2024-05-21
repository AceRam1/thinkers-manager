package ThinkersUserManagement;

public class schedules {

        private int scheduleId;
        private int tutorId;
        private String tutorName; // Change from int to String for tutorId
        private int tuteeId;
        private String subjects;
        private String time;
        private String day;
        private String month;
        private String year;
        private String startDate;
        private String endDate;
        private String paymentStatus; // Added paymentStatus field

    
        // Constructor

        public schedules(int scheduleId, int tutorId, String tutorName, int tuteeId, String subjects,
        String time, String day, String month, String year, String startDate, String endDate, String paymentStatus) {
            this.scheduleId = scheduleId;
            this.tutorId = tutorId;
            this.tutorName = tutorName;
            this.tuteeId = tuteeId;
            this.subjects = subjects;
            this.time = time;
            this.day = day;
            this.month = month;
            this.year = year;
            this.startDate = startDate;
            this.endDate = endDate;
            this.paymentStatus = paymentStatus;
        }

        public schedules(int scheduleId, int tutorId, int tuteeId, String subjects,
                            String time, String day, String month, String year, String paymentStatus) {
            this.scheduleId = scheduleId;
            this.tutorId = tutorId;
            this.tuteeId = tuteeId;
            this.subjects = subjects;
            this.time = time;
            this.day = day;
            this.month = month;
            this.year = year;
            this.paymentStatus = paymentStatus;

        }
    
        public schedules(int scheduleId, int tutorId, String subjects, String time, String day, String month, String year, String paymentStatus) {
            this.scheduleId = scheduleId;
            this.tutorId = tutorId;
            this.subjects = subjects;
            this.time = time;
            this.day = day;
            this.month = month;
            this.year = year;
            this.paymentStatus = paymentStatus;

        }

        // Constructor with tutorName parameter
        public schedules(int scheduleId, String tutorName, String subjects,
                        String time, String day, String month, String year, String paymentStatus) {
            this.scheduleId = scheduleId;
            this.tutorName = tutorName;
            this.subjects = subjects;
            this.time = time;
            this.day = day;
            this.month = month;
            this.year = year;
            this.paymentStatus = paymentStatus;

}

        public schedules(String subjects, String startDate, String endDate) {
            this.subjects = subjects;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        // Getters and setters
        public String getPaymentStatus() {
            return paymentStatus;
        }
    
        // Setter for paymentStatus
        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public int getScheduleId() {
            return scheduleId;
        }
    
        public void setScheduleId(int scheduleId) {
            this.scheduleId = scheduleId;
        }
    
        public int getTutorId() {
            return tutorId;
        }
    
        public void setTutorId(int tutorId) {
            this.tutorId = tutorId;
        }
    
        public int getTuteeId() {
            return tuteeId;
        }
    
        public void setTuteeId(int tuteeId) {
            this.tuteeId = tuteeId;
        }
    
        public String getSubjects() {
            return subjects;
        }
    
        public void setSubjects(String subjects) {
            this.subjects = subjects;
        }
    
        public String getTime() {
            return time;
        }
    
        public void setTime(String time) {
            this.time = time;
        }
    
        public String getDay() {
            return day;
        }
    
        public void setDay(String day) {
            this.day = day;
        }
    
        public String getMonth() {
            return month;
        }
    
        public void setMonth(String month) {
            this.month = month;
        }
    
        public String getYear() {
            return year;
        }
    
        public void setYear(String year) {
            this.year = year;
        }

        public String getTutorName() {
            return tutorName;
        }
        
        public void setTutorName(String tutorName) {
            this.tutorName = tutorName;
        }

        public String getStartDate() {
            return startDate;
        }
    
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    
        public String getEndDate() {
            return endDate;
        }
    
        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }
    
