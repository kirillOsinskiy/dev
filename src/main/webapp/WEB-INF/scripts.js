/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

