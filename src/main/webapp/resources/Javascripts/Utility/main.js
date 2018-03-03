var Language;
function SynchronousPostAjaxRequest(Url, Paramas, ProgressObject) {
    try {
        if (ProgressObject != null) {
            ProgressObject.progressOn();
        }
        var response = window.dhx.ajax.postSync(Url, Paramas);
        var json = window.dhx.s2j(response.xmlDoc.responseText);
        if (ProgressObject != null) {
            ProgressObject.progressOff();
        }
        return json;
    } catch (e) {
        console.log(e);
        if (ProgressObject != null) {
            ProgressObject.progressOff();
        }
        return null;
    }
}
function SynchronousGetAjaxRequest(Url, Params, ProgressObject) {
    try {
        if (ProgressObject != null) {
            ProgressObject.progressOn();
        }
        if(Params == null || Params == ""){
            
            Url = encodeURI(Url);
        }
        else{
              Url = encodeURI(Url);
              Params = encodeURI(Params);
            
        }
        var response = window.dhx.ajax.getSync(Url, Params);
        var json = window.dhx.s2j(response.xmlDoc.responseText);
        if (ProgressObject != null) {
            ProgressObject.progressOff();
        }
        return json;
    } catch (e) {
        console.log(e);
        if (ProgressObject != null) {
            ProgressObject.progressOff();
        }
        return null;
    }
}
function progressOffCustom(ProgressObject) {
    ProgressObject.progressOff();
}
function ItemNameValidation(data) {
    if (data === "" || data === null || (data.length > 20 || data.length < 3)) {
        return false;
    } else {
        var Response = SynchronousGetAjaxRequest("isValueUnique?VALUE=" + data.trim() + "&TABLE_NAME=ITEMS&COLUMN_NAME=ITEM_NAME", "", null);
        if (Response.RESPONSE_STATUS === "SUCCESS") {
            if (Response.RESPONSE_VALUE.UNIQUE === "TRUE") {
                showSuccessNotification(Response.RESPONSE_MESSAGE);
                return true;
            } else {
                showFailedNotification(Response.RESPONSE_MESSAGE);
                return false;
            }

        } else {
            showFailedNotification("Request Error, Check Database Connection");
        }
    }
}
function UpdatedItemNameValidation(data) {
    if (data === "" || data === null || (data.length > 20 || data.length < 3)) {
        return false;
    } else {
        var Response = SynchronousGetAjaxRequest("isValueUnique?VALUE=" + data.trim() + "&TABLE_NAME=ORDER_ITEMS&COLUMN_NAME=ITEM_NAME", "", null);
        if (Response.RESPONSE_STATUS === "SUCCESS") {
            if (Response.RESPONSE_VALUE.UNIQUE === "TRUE") {
                showSuccessNotification(Response.RESPONSE_MESSAGE);
                return true;
            } else {
                showFailedNotification(Response.RESPONSE_MESSAGE);
                return false;
            }

        } else {
            showFailedNotification("Request Error, Check Database Connection");
        }
    }
}
function UserNameValidation(data) {
    if (data === "" || data === null || (data.length > 20 || data.length < 3)) {
        return false;
    } else {
        var Response = SynchronousGetAjaxRequest("isValueUnique?VALUE=" + data.trim() + "&TABLE_NAME=USERS&COLUMN_NAME=USER_ID", "", null);
        if (Response.RESPONSE_STATUS === "SUCCESS") {
            if (Response.RESPONSE_VALUE.UNIQUE === "TRUE") {
                showSuccessNotification(Response.RESPONSE_MESSAGE);
                return true;
            } else {
                showFailedNotification(Response.RESPONSE_MESSAGE);
                return false;
            }

        } else {
            showFailedNotification("Request Error, Check Database Connection");
        }
    }
}

function OrderBillNoValidation(data) {
        if (data === "" || data === null || (data.length > 10 || data.length < 1)) {
        return false;
    } else {
        var Response = SynchronousGetAjaxRequest("isValueUnique?VALUE=" + data.trim() + "&TABLE_NAME=ORDERS&COLUMN_NAME=BILL_NO", "", null);
        if (Response.RESPONSE_STATUS === "SUCCESS") {
            if (Response.RESPONSE_VALUE.UNIQUE === "TRUE") {
                showSuccessNotification(Response.RESPONSE_MESSAGE);
                return true;
            } else {
                showFailedNotification(Response.RESPONSE_MESSAGE);
                return false;
            }

        } else {
            showFailedNotification("Request Error, Check Database Connection");
        }
    }

}
function showSuccessNotification(data) {
    dhtmlx.message({
        text: data,
        expire: 5000,
        type: "SuccessNotification"
    });
}
function showFailedNotification(data) {
    dhtmlx.message({
        text: data,
        expire: 5000,
        type: "error"
    });
}
function showSuccessNotificationWithICON(data) {
    dhtmlx.message({
        text:"<div><div style='float:left;margin-right:5px;vertical-align:middle;' ><img src='resources/Images/ok.png' height='30px' width='30px'></div><div >"+data+"</div></div>",
        expire: 5000,
        type: "SuccessNotification"
    });
}
function showFailedNotificationWithICON(data) {
    dhtmlx.message({
        text:"<div><div style='float:left;margin-right:5px;vertical-align:middle;' ><img src='resources/Images/failed.png' height='30px' width='30px'></div><div >"+data+"</div></div>",
        expire: 5000,
        type: "error"
    });
}

function loadLanguagePack() {
    var Response = SynchronousGetAjaxRequest("getLanguage", "", null);
    Language = Response.RESPONSE_VALUE.LANGUAGE_PACK;
}
function dateValidator(data) {
    return (data instanceof Date);
}
function dateCheck(data) {
    var regex = /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/;
    return regex.test(data);
}

function getCurrentDate() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    var today = dd + '/' + mm + '/' + today.getFullYear().toString().substring(2,4);
    return today;
}
function isCompositeValueUnique(TABLE_NAME,COLUMN_NAME1,COLUMN_NAME2,VALUE1,VALUE2) {    
    if (false) {//Conditions later
        return false;
    } else {
        var Response = SynchronousGetAjaxRequest("isCompositeValueUnique?TABLE_NAME="+TABLE_NAME.trim()+"&COLUMN_NAME1="+COLUMN_NAME1.trim()+"&COLUMN_NAME2="+COLUMN_NAME2.trim()+"&VALUE1="+VALUE1+"&VALUE2="+VALUE2, "", null);
        if (Response.RESPONSE_STATUS === "SUCCESS") {
            if (Response.RESPONSE_VALUE.UNIQUE === "TRUE") {
                showSuccessNotification(Response.RESPONSE_MESSAGE);
                return true;
            } else {
                showFailedNotification(Response.RESPONSE_MESSAGE);
                return false;
            }

        } else {
            showFailedNotification("Request Error, Check Database Connection");
        }
    }
}
