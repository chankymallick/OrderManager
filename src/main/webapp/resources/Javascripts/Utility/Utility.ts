/// <reference path="../Home/OrderManagerHome.ts"/>
declare var progressOffCustom: any;
declare var shortcut: any;
declare var showSuccessNotification: any;
declare var showFailedNotification: any;
declare var SynchronousGetAjaxRequest: any;
declare var showSuccessNotificationWithICON: any;
declare var showFailedNotificationWithICON: any;
declare var Language:any;
module com.ordermanager.utilty {
    export class MainUtility {
    }
    export class FormEntryManager {
        public DataEntryLayoutCell: any;
        public NotificationCell: any;
        public ModifiedLayoutObject: any;
        public FormName: any;
        public FormObject: any
        public MainUtilityObj: MainUtility;
        public OperationToolbar: any;
        public DataEntryLayoutCellHeight: any;
        public DataViewLayoutCellHeight: any;
        public DataTabber: any;
        public DataViewGridObject: any;
        public OrderManagerHomeOBJ: com.ordermanager.home.OrderManagerHome;
        public DefualtDataFormObject: any;
        public isDefaultOn = false;
        constructor(layoutCell: any, notificationCell: any, formname: any, dataentryCellHeight: any, dataviewcellheight: any) {
            this.DataEntryLayoutCellHeight = dataentryCellHeight;
            this.DataViewLayoutCellHeight = dataviewcellheight;
            this.DataEntryLayoutCell = layoutCell;
            this.NotificationCell = notificationCell;
            this.FormName = formname;
            this.constructInnerLayoutforDataEntry();
            this.MainUtilityObj = new com.ordermanager.utilty.MainUtility();
            this.shortCutRegister();

        }
        public shortCutRegister() {
            shortcut.add("F8", () => {
                this.validateAndSaveFormData();
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document,
                    'propagate': true
                });
        }

        public constructInnerLayoutforDataEntry() {
            this.ModifiedLayoutObject = this.DataEntryLayoutCell.attachLayout({
                pattern: "2E",
                cells: [
                    { id: "a", text: "Data", header: false },
                    { id: "b", text: "Existing Data", header: false, height: this.DataViewLayoutCellHeight }
                ]
            });
            this.ModifiedLayoutObject.progressOn();
            this.ModifiedLayoutObject.cells("b").progressOn();
            this.DataTabber = this.ModifiedLayoutObject.cells("a").attachTabbar();
            this.DataTabber.addTab("data", "Data Entry", null, null, true, false);
            this.DataTabber.addTab("default", "Set Default Data", null, null, false, false);
            this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
            this.OperationToolbar.loadStruct("operationToolbar", "json");
            this.OperationToolbar.attachEvent("onClick", (id) => {
                if (id === "clear") {
                    this.FormObject.clear();
                }
                if (id === "new") {
                    this.setFormStateNewRecord();
                }
                if (id === "save") {
                    this.validateAndSaveFormData();
                }
                if (id === "save_default") {
                    this.saveDefualtFormValue("FORM_DEFAULT_VALUE", this.FormName);
                }

            });
            this.OperationToolbar.attachEvent("onStateChange", (id, state) => {
                if (id === "default") {
                    this.isDefaultOn = state;
                    this.FormInitialization();
                }
            });
            this.FormInitialization();
            this.FormObject.attachEvent("onXLE", () => {
                progressOffCustom(this.ModifiedLayoutObject);
            });
            // this.FormObject.attachEvent("onKeyUp",(inp, ev, name, value)=>{
            // });
            this.DataViewGridObject = this.ModifiedLayoutObject.cells("b").attachGrid();
            this.DataViewGridObject.load("LoadDataViewGrid?gridname=" + this.FormName);
            this.DataViewGridObject.attachEvent("onXLE", () => {
                progressOffCustom(this.ModifiedLayoutObject.cells("b"));
            });
            this.DataViewGridObject.attachEvent("onRowSelect", (id, ind) => {
            });
        }


        public FormInitialization() {
            if (this.FormObject != null || this.FormObject != undefined) {
                this.FormObject.unload();
            }
            this.FormObject = this.DataTabber.tabs("data").attachForm();
            this.FormObject.load(this.FormName + "?Default=" + this.isDefaultOn);
            this.FormObject.enableLiveValidation(true);
            if (this.DefualtDataFormObject != null || this.DefualtDataFormObject != undefined) {
                this.DefualtDataFormObject.unload();
            }
            this.DefualtDataFormObject = this.DataTabber.tabs("default").attachForm();
            this.DefualtDataFormObject.load(this.FormName + "?Default=true");
            this.DefualtDataFormObject.enableLiveValidation(true);
        }
        public setFormStateAfterSave() {
            this.FormObject.lock();
            this.OperationToolbar.enableItem("new");
            this.OperationToolbar.disableItem("save");
            this.OperationToolbar.disableItem("clear");
            this.DataViewGridObject.load("LoadDataViewGrid?gridname=" + this.FormName);
            shortcut.add("F4", () => {
                this.setFormStateNewRecord();
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document,
                    'propagate': true
                });
            shortcut.remove("F8");
        }
        public setFormStateNewRecord() {
            this.FormInitialization();
            this.OperationToolbar.disableItem("new");
            this.OperationToolbar.enableItem("save");
            this.OperationToolbar.enableItem("clear");
            shortcut.remove("F4");
            shortcut.add("F8", () => {
                this.validateAndSaveFormData();
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document,
                    'propagate': true
                });
        }
        public saveDefualtFormValue(module_name: any, key_name: any) {
            this.DefualtDataFormObject.updateValues();
            this.DataEntryLayoutCell.progressOn();
            var Response = SynchronousGetAjaxRequest("saveUpdateDefaultFormValue?VALUE=" + JSON.stringify(this.DefualtDataFormObject.getValues()) + "&MODULE=" + module_name + "&KEY=" + key_name, "", null);
            if (Response.RESPONSE_STATUS === "SUCCESS") {
                showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                this.NotificationCell.collapse();
            }
            if (Response.RESPONSE_STATUS === "FAILED") {
                showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                this.NotificationCell.attachHTMLString("<b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                this.NotificationCell.expand();
            }
            this.DataEntryLayoutCell.progressOff();
        }
        public validateAndSaveFormData() {
            this.setSpecificBeforeSave();
            if (this.FormObject.validate()) {
                this.FormObject.updateValues();
                this.DataEntryLayoutCell.progressOn();
                var Response = SynchronousGetAjaxRequest("addItem?ParamData=" + JSON.stringify(this.FormObject.getValues()), "", null);
                if (Response.RESPONSE_STATUS === "SUCCESS") {
                    showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                    this.setFormStateAfterSave();
                    this.NotificationCell.collapse();
                    this.DataEntryLayoutCell.progressOff();
                }
                if (Response.RESPONSE_STATUS === "FAILED") {
                    showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                    this.NotificationCell.attachHTMLString("<b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                    this.NotificationCell.expand();
                    progressOffCustom(this.DataEntryLayoutCell);
                }
            }
            else {
                showFailedNotification("Data Validation Error")
            }
        }
        public setSpecificFormSettingsoNLoad() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ITEM) {

            }
        }
        public setSpecificBeforeSave() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ITEM) {

            }
        }
        public setSpecificAfterSave() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ITEM) {

            }
        }
    }
}
