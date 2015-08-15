/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DriverManager {
    /**
     * Коллекция заменяющая БД
     */
    private static Map<UUID,Driver> guests;
    
    /**
     * Водители в табличке на форме
     */
    private static Map<UUID,Driver> selectedGuests;

    public DriverManager() {
        guests = new HashMap<>();
        selectedGuests = new HashMap<>();
        prepareData();
    }    
    
    /**
     * Добавить водителя в "базу"
     * @param guest 
     */
    public void addGuest(Driver guest) {
        guests.put(guest.getId(),guest);
    }
    /**
     * Добавить водителя к списку отображаемых на форме
     * @param guestData ФИО из autocomplete поля
     */
    public void addSelectedGuest(String guestData) {        
        for(Driver g : guests.values()) {
            if(g.getFIOAndBirthDay().equals(guestData)) {
                selectedGuests.put(g.getId(),g);
                break;
            }
        }        
    }
    /**
     * Вернуть всех водителеей
     * @return 
     */
    public Collection<Driver> getAllGuests() {
        return guests.values();
    }
    /**
     * Вернуть классы водителей
     * @return 
     */
    public List<String> getDriverClasses() {
        List<String> res = new ArrayList<>();
        res.add("А");
        res.add("Б");
        res.add("В");
        res.add("Г");
        return res;
    }
    /**
     * Удалить водителя из "базы"
     * @param id 
     */
    void removeGuest(String id) {
        guests.remove(UUID.fromString(id));
    }
    /**
     * Удалить водителя с формы
     * @param id 
     */
    void removeSelectedGuest(String id) {        
        selectedGuests.remove(UUID.fromString(id));
    }
    /**
     * Вернуть всех для autocomplete поля
     * @return 
     */
    List<String> getGuestsAC() {
        List<String> res = new ArrayList<>();
        for(Driver g : guests.values()) {
            res.add(g.getFIOAndBirthDay());
        }
        return res;
    }
    /**
     * Вернуть водителей для autocomplete поля найденным по ФИО
     * @param fio ФИО для поиска водителей
     * @return 
     */
    List<String> findByFIO(String fio) {
        List<String> res = new ArrayList<>();
        for(Driver g : guests.values()) {
            if(g.getFIO().contains(fio)) {
                res.add(g.getFIOAndBirthDay());
            }
        }
        return res;
    }
    /**
     * Подготовить данные для "базы"
     */
    private void prepareData() {
        addGuest(new Driver("Иванов", "Иван", "Иванович", Sex.MALE, "А"));
        addGuest(new Driver("Семенова", "Вера", "Перовна", Sex.FEMALE, "Б"));
        addGuest(new Driver("Ебрагимова", "Татьяна", "Валерьевна", Sex.FEMALE, "А"));
        addGuest(new Driver("Петров", "Евгений", "Олегович", Sex.MALE, "В"));
        addGuest(new Driver("Голубчиков", "Виктор", "Владимирович", Sex.MALE, "В"));
        addGuest(new Driver("Иванова", "Надежда", "Павловна", Sex.FEMALE, "Б"));
        addGuest(new Driver("Павленко", "Михаил", "Валерьевич", Sex.MALE, "Г"));
        addGuest(new Driver("Замятин", "Андрей", "Михайлович", Sex.MALE, "Г"));
        addGuest(new Driver("Шадрина", "Ольга", "Анатольевна", Sex.FEMALE, "Г"));
    }    
    /**
     * Вернуть водителей в табличке на форме
     * @return 
     */
    Collection<Driver> getSelectedGuests() {
        return selectedGuests.values();
    }    
    /**
     * Сохранить водителя и вывести его в табличку на форму
     * @param driver 
     */
    void saveDriver(Driver driver) {
        guests.put(driver.getId(),driver);
        selectedGuests.put(driver.getId(),driver);
    }
}
