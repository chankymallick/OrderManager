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
function SynchronousGetAjaxRequest(Url, Paramas, ProgressObject) {
    try {
        if (ProgressObject != null) {
            ProgressObject.progressOn();
        }

        var response = window.dhx.ajax.getSync(Url, Paramas);
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
function showSuccessNotification(data) {
    dhtmlx.message({
    text:data,
    expire:5000,
        type: "SuccessNotification"
    });
}
function showSuccessNotificationWithICON(data) {
    dhtmlx.message({
        text: "<img height='30px' width='30px' style='margin-right:5px;vertical-align:middle;'  src='resources/Images/ok.png'/><span>"+data+"</span>",
        expire: 5000,
        type: "SuccessNotification"
    });
}
function showFailedNotificationWithICON(data) {
    dhtmlx.message({
        text: "<img height='30px' width='30px' style='margin-right:5px;vertical-align:middle;'  src='resources/Images/failed.png'/><span>"+data+"</span>",
        expire: 5000,
        type: "error"
    });
}
function showFailedNotification(data) {
    dhtmlx.message({
        text: data,
        expire: 5000,
        type: "error"
    });
}
function loadLanguagePack(){
    var Response = SynchronousGetAjaxRequest("getLanguage","",null);    
    Language = Response.RESPONSE_VALUE.LANGUAGE_PACK;    
}
function dateValidator(data){
  return (data instanceof Date);
}
function dateCheck(data){
  var regex = /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/;  
  return regex.test(data);  
}