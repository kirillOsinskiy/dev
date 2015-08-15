package driver;
 
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
public class DriverController {
    
    private final DriverManager driverManager;

    public DriverController(DriverManager driverManager) {
        this.driverManager = driverManager;        
    }    
    /**
     * Показать форму
     * @return 
     */
    @RequestMapping(value="/guest")
    public ModelAndView addGuest()  {        
        return new ModelAndView("guest.jsp");
    }
    /**
     * Добавить водителя в табличку на форме
     * @param request
     * @return
     * @throws UnsupportedEncodingException 
     */    
    @RequestMapping(value="/guestSelAdd")
    public @ResponseBody Collection<Driver> addSelectedGuest(HttpServletRequest request) 
            throws UnsupportedEncodingException {        
        request.setCharacterEncoding("UTF-8");
        String guestData = request.getParameter("guestData");
        if (guestData != null) {
            driverManager.addSelectedGuest(guestData);
        }
        return driverManager.getSelectedGuests();
    }
    /**
     * Вернуть справочник классов водителей
     * @return 
     */
    @RequestMapping(value="/driverClasses")
    public @ResponseBody List<String> getDriverClasses() {
        return driverManager.getDriverClasses();
    }
    /**
     * Вернуть всех водителей добалвенных в табличку на форму
     * @return 
     */
    @RequestMapping(method=RequestMethod.GET, value ="/guestSelected")  
    public @ResponseBody Collection<Driver> getSelectedGuests(){ 
        return driverManager.getSelectedGuests();
    }
    /**
     * Найти водителей для поля поиска по совпадениям с ФИО
     * @param request
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value ="/guestsAc")  
    public @ResponseBody List<String> getGuestsAC(HttpServletRequest request) 
            throws UnsupportedEncodingException{
        request.setCharacterEncoding("UTF-8");
        String fio = request.getParameter("fio");
        if(fio != null && !fio.isEmpty()) {
            return driverManager.findByFIO(fio);
        }
        return driverManager.getGuestsAC();
    }
    /**
     * Удалить водителя из таблички
     * @param request
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(method=RequestMethod.POST, value="/guestDelete")
    public @ResponseBody Collection<Driver> deleteGuest(HttpServletRequest request) 
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String guestID = request.getParameter("guestID");
        if (guestID != null) {
            driverManager.removeSelectedGuest(guestID);
        }
        return driverManager.getSelectedGuests();
    }
    /**
     * Добавить или сохранить нового водителя в "БД"
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParseException 
     */
    @RequestMapping(method=RequestMethod.POST, value="/driverSave")
    public @ResponseBody Collection<Driver> driverSave(HttpServletRequest request) 
            throws UnsupportedEncodingException, ParseException {
        request.setCharacterEncoding("UTF-8");
        Driver driver = parseFromReq(request);
        if(!driver.getLastName().isEmpty() && !driver.getFirstName().isEmpty()
            && !driver.getMiddleName().isEmpty() && !driver.getBirthDate().isEmpty()) {
            driverManager.saveDriver(driver);        
        }        
        return driverManager.getSelectedGuests();
    }
    /**
     * Формирует объект фодителя из запроса
     * @param request
     * @return
     * @throws ParseException 
     */
    private Driver parseFromReq(HttpServletRequest request) throws ParseException {
        Driver d = new Driver();
        if(request.getParameter("driver[id]") != null &&
            !request.getParameter("driver[id]").isEmpty()) {
            d.setId(UUID.fromString(request.getParameter("driver[id]")));
        }
        d.setLastName(request.getParameter("driver[lastName]"));
        d.setFirstName(request.getParameter("driver[firstName]"));
        d.setMiddleName(request.getParameter("driver[middleName]"));
        d.setBirthDate(request.getParameter("driver[birthDate]"));
        d.setSex(request.getParameter("driver[sex]"));
        d.setDriverClass(request.getParameter("driver[driverClass]"));
        return d;
    }
    
}