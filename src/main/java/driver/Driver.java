package driver;
 
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
 
public class Driver implements Serializable {       
    
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date birthDate;
    private Sex sex;
    private String driverClass;
    
    final private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
 
    // Constructors:
    public Driver() {
        id = UUID.randomUUID();
    }
    
    public Driver(String name) {
        id = UUID.randomUUID();
        this.firstName = name;        
        birthDate = randDate();
    }
    
    public Driver(String lastName, String firstName, String middleName, Sex sex, String driverClass) {
        id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = randDate();
        this.sex = sex;
        this.driverClass = driverClass;
    }

    public Driver(String lastName, String firstName, String middleName, Date birthDate, Sex sex, String driverClass) {
        id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.driverClass = driverClass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthDate() {
        return sdf.format(birthDate);
    }

    public void setBirthDate(String birthDate) throws ParseException {
        this.birthDate = sdf.parse(birthDate);
    }

    public String getSex() {        
        return sex.toString();
    }
    
    public void setSex(String sex) {
        if(sex.equals(Sex.MALE.toString())) {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
    
    /**
     * Метод опредления возраста
     * @return 
     */
    public int getAge() {        
        Calendar cur = Calendar.getInstance();
        Calendar birth = Calendar.getInstance();        
        birth.setTime(birthDate);
        cur.add(Calendar.YEAR, -1*birth.get(Calendar.YEAR));
        cur.add(Calendar.MONTH, -1*birth.get(Calendar.MONTH));
        cur.add(Calendar.DAY_OF_MONTH, -1*birth.get(Calendar.DAY_OF_MONTH));
        return cur.get(Calendar.YEAR);
    }
    
    public void setAge(int age) {
        
    }
    
    // String Representation:
    @Override
    public String toString() {
        return String.valueOf(id);
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Driver other = (Driver) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    private Date randDate() {
        long offset = Timestamp.valueOf("1960-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("1995-01-01 00:00:00").getTime();
        long diff = end - offset + 1;        
        return new Date(offset + (long)(Math.random() * diff));
    }

    String getFIO() {
        StringBuilder sb = new StringBuilder();
        sb.append(lastName).append(" ")
          .append(firstName).append(" ")
          .append(middleName);
        return sb.toString();
    }

    String getFIOAndBirthDay() {
        StringBuilder sb = new StringBuilder();
        sb.append(lastName).append(" ")
          .append(firstName).append(" ")
          .append(middleName).append(" ")
          .append(getBirthDate());
        return sb.toString();
    }
}
