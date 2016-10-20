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
