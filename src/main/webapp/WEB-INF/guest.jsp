<!--%@page import="java.util.List"%-->
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!--%@page import="guest.*"%-->

<!--jsp:useBean id="guestList" type="Guest[]" scope="request" /-->

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Drivers base</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script src= "http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>        
        <style>
            table, th , td {
                border: 1px solid grey;
                border-collapse: collapse;
                padding: 5px;
            }
            table tr:nth-child(odd) {
                background-color: #f1f1f1;
            }
            table tr:nth-child(even) {
                background-color: #ffffff;
            }
        </style> 
    </head>

    <body ng-app="myApp" ng-controller="searchCtrl">
        <!--form method="POST" action="guest.html">
            Name: <input type="text" name="name" />
            <input type="submit" value="Add" />
        </form-->
        <hr>
        <div >
            <table ng-table="myTable">
                <tr>
                    <td>Фамилия</td>
                    <td>Имя</td>
                    <td>Отчество</td>
                    <td>Дата рождения</td>
                    <td>Возраст</td>
                    <td>Пол</td>
                    <td>Класс</td>
                    <td colspan="2"></td>
                </tr>
                <tr ng-repeat="guest in guests">
                    <td>{{ guest.lastName}}</td>
                    <td>{{ guest.firstName}}</td>
                    <td>{{ guest.middleName}}</td>
                    <td>{{ guest.birthDate}}</td>
                    <td>{{ guest.age}}</td>
                    <td>{{ guest.sex}}</td>
                    <td>{{ guest.driverClass}}</td>
                    <td>
                        <button ng-click="editDriver(guest.id)">Edit</button>
                    </td>
                    <td>                       
                        <button ng-click="deleteDriver(guest.id)">X</button>
                    </td>
                </tr>
            </table>
        </div>
        <hr>
        <div class="ui-widget">            
            <label for="tags">Поиск: </label>
            <input id="tags" name="fio" style="width:450px" charset="UTF-8" 
                   onkeypress="return getDataForAutoComplete(event)" />            
        </div>
        <hr>
        <!--div ng-app="myApp" ng-controller="editCtrl"-->
        <div>
                <table ng-table="editTable">
                    <tr>
                        <td>Фамилия</td>
                        <td><input id="lastName" type="text" /></td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td><input id="firstName" type="text" /></td>
                    </tr>
                    <tr>
                        <td>Отчество</td>
                        <td><input id="middleName" type="text" /></td>
                    </tr>
                    <tr>
                        <td>Дата рождения</td>
                        <td>
                            <input id="birthDate" type="text" 
                                   onchange="setAges()"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Возраст</td>
                        <td><input id="age" type="text" readonly="true"/></td>
                    </tr>
                    <tr>
                        <td>Пол</td>
                        <td>
                            <form action="">
                                <input type="radio" id="male" name="sex" 
                                       value="male">Муж.
                                <input type="radio" id="female" name="sex" 
                                       value="female">Жен.
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>Класс</td>
                        <td>
                            <select id="driverClass" ng-model="selectedItem" 
                                    ng-options="dc as dc for dc in dcs">                                        
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="hidden" id="diverID" />
                            <button ng-click="saveDriver()">Save</button>
                        </td>
                    </tr>
                </table>            
        </div>

        <script  charset="UTF-8" >
            var app = angular.module('myApp', []);
            app.controller('searchCtrl', function($scope) {
                    getSelGuests(),
                    getDriverClasses(),
                    $scope.deleteDriver = function (id) {
                        deleteDriver(id);
                    },
                    $scope.editDriver = function (id) {
                        showInEditForm(id);
                    },
                    $scope.saveDriver = function () {
                        saveDriver();
                    };
            });
            
            function addSelGuest(guestData) {
                $.ajax({
                    type: 'post',
                    url: '/Driverbook/guestSelAdd.html',
                    data: {'guestData': guestData},
                    response: 'text',
                    success: function (data) {
                        refreshTable(data);
                                $("#tags").val("");
                        }
                });
            }

            function showInEditForm(id) {
                var scope = angular.element($('[ng-controller="searchCtrl"]')).scope();
                angular.forEach(scope.guests, function(value, index){
                    if (id == value.id) {
                        fillEdt(value, scope);
                    }
                });
            }

            function fillEdt(driver, scope) {
                $("#lastName").val(driver.lastName);
                $("#firstName").val(driver.firstName);
                $("#middleName").val(driver.middleName);
                $("#birthDate").val(driver.birthDate);
                setAges();
                if (driver.sex == "М") {
                    document.getElementById("male").checked = true;
                } else {
                    document.getElementById("female").checked = true;
                }
                scope.selectedItem = driver.driverClass;
                $("#diverID").val(driver.id);
            }

            function saveDriver() {
                var driver = getDriverFromForm();
                if(driver.lastName == "" || 
                   driver.firstName == "" ||
                   driver.middleName == "" ||
                   driver.birthDate == "") {
                   alert("Нельзя сохранить человека без ФИО и даты рождения!");
                   return false;
                }
                $.ajax({
                    type: 'post',
                    url: '/Driverbook/driverSave.html',
                    data: {'driver': driver},
                    response: 'text',
                    success: function (data) {
                    refreshTable(data);
                            cleanEditForm();
                    }
                });
            }

            function cleanEditForm() {
                $("#diverID").val("");
                $("#lastName").val("");
                $("#firstName").val("");
                $("#middleName").val("");
                $("#birthDate").val("");
                $("#age").val("");
                document.getElementById("male").checked = false;
                document.getElementById("female").checked = false;
            }

            function getDriverFromForm() {
                var scope = angular.element($('[ng-controller="searchCtrl"]'))
                    .scope();
                var driver =
                    {
                        "id": $("#diverID").val().trim(),
                        "lastName": $("#lastName").val().trim(),
                        "firstName": $("#firstName").val().trim(),
                        "middleName": $("#middleName").val().trim(),
                        "birthDate": $("#birthDate").val(),
                        "sex": document.getElementById("male").checked ? "М" : "Ж",
                        "driverClass": scope.selectedItem
                    };
                return driver;
            }
            
            function deleteDriver(guestId) {
                $.ajax({
                    type: 'post',
                    url: 'guestDelete.html',
                    data: {'guestID': guestId},
                    response: 'text',
                    success: function (data) {
                        refreshTable(data);
                    }
                 });
            }

            function getSelGuests() {
                $.ajax({
                    type: 'get',
                    url: '/Driverbook/guestSelected.html',
                    data: null,
                    response: 'text',
                    success: function (data) {
                    refreshTable(data);
                    }
                });
            }

            function getDriverClasses() {
                $.ajax({
                    type: 'get',
                    url: '/Driverbook/driverClasses.html',
                    data: null,
                    response: 'text',
                    success: function (data) {
                        var scope = angular.element($('[ng-controller="searchCtrl"]')).scope();
                        scope.$apply(function () {
                            scope.dcs = data;                            
                        });
                    }
                });
            }

            function getDataForAutoComplete(e) {
                if (e.keyCode == 13) {
                    var guestData = document.getElementById('tags').value; 
                    if (guestData.length == 0) {
                        return false;
                    }
                    $.ajax({
                        type: 'post',
                        url: '/Driverbook/guestsAc.html',
                        data: {'fio': guestData},
                        response: 'text',
                        success: function (data) {
                            if (data != null && data != "") {
                                $("#tags").autocomplete({
                                    source: data,
                                    minLength: 2,
                                    select: function (a, b) {
                                        addSelGuest(b.item.value)
                                    }
                                });
                            } else {
                                alert("Не найдено водителей для '"
                                + guestData + "'!");
                            }
                        }
                    });
                    return true;
                }
            }

            function refreshTable(data) {
                var scope = angular.element($('[ng-controller="searchCtrl"]')).scope();
                scope.$apply(function () {
                    scope.guests = data;
                });
            }

            function setAges() {
                var curDate = new Date($.now());
                var birthDate = new Date($("#birthDate").datepicker("getDate"));
                var diff = curDate - birthDate;
                var years = Math.floor(diff / 31536000000);
                $("#age").val(years);
            }

            $(function () {
                $("#birthDate").datepicker({
                    dateFormat: 'dd.mm.yy',
                    minDate: '-100Y',
                    maxDate: '-16Y'
                });
            });
        </script>
    </body>
</html>