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
function ItemName(data) {
    if (data === "" || data === null || (data.length > 20 || data.length < 3)) {
        return false;
    } else {
        return true;
    }
}
function showSuccessNotification(data){
    dhtmlx.message({
    text:data,
    expire:5000,
    type: "SuccessNotification"
});
}
function showFailedNotification(data){
    dhtmlx.message({
    text:data,
    expire:5000,
    type: "error"
});
}
