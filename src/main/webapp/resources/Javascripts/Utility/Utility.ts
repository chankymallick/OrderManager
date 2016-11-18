/// <reference path="../Home/OrderManagerHome.ts"/>
declare var progressOffCustom: any;
declare var shortcut: any;
declare var showSuccessNotification: any;
declare var showFailedNotification: any;
declare var SynchronousGetAjaxRequest: any;
declare var showSuccessNotificationWithICON: any;
declare var showFailedNotificationWithICON: any;
declare var Language: any;
declare var dhtmlXWindows: any;
declare var getCurrentDate: any;
module com.ordermanager.utilty {
    export class MainUtility {
        public getModelWindow(HeaderText: any, Height: any, Width: any) {
            var myWins = new dhtmlXWindows();
            myWins.createWindow("win1", 50, 50, Height, Width);
            myWins.window("win1").denyPark();
            myWins.window("win1").denyResize();
            myWins.window("win1").center();
            myWins.window("win1").setModal(true);
            myWins.window("win1").setText(HeaderText);
            return myWins.window("win1");
        }
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
        public GlobalFormJSONValues = {};
        public SelectedItemNameList = [];
        public CustomRate: any;
        constructor(layoutCell: any, notificationCell: any, formname: any, dataentryCellHeight: any, dataviewcellheight: any) {
            this.DataEntryLayoutCellHeight = dataentryCellHeight;
            this.DataViewLayoutCellHeight = dataviewcellheight;
            this.DataEntryLayoutCell = layoutCell;
            this.NotificationCell = notificationCell;
            this.FormName = formname;
            this.constructInnerLayoutforDataEntry();
            this.MainUtilityObj = new com.ordermanager.utilty.MainUtility();
            this.shortCutRegister();
            this.setSpecificFormSettingsoNLoad();

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
            this.OperationToolbar.loadStruct("operationToolbar?formname=" + this.FormName, "json");
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
            // this.FormObject.attachEvent("onXLE", () => {
            //     this.FormObject.setFocusOnFirstActive();
            //     this.FormObject.keyPlus();
            //     progressOffCustom(this.ModifiedLayoutObject);
            // });
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
            this.FormObject.load(this.FormName + "_Form?Default=" + this.isDefaultOn);
            this.FormObject.enableLiveValidation(true);
            this.FormObject.attachEvent("onXLE", () => {
                //this.FormObject.setFocusOnFirstActive();
                this.FormObject.setFocusOnFirstActive();
                this.FormObject.keyPlus();
                progressOffCustom(this.ModifiedLayoutObject);
            });
            //this.FormObject.keyPlus();
            if (this.DefualtDataFormObject != null || this.DefualtDataFormObject != undefined) {
                this.DefualtDataFormObject.unload();
            }
            this.DefualtDataFormObject = this.DataTabber.tabs("default").attachForm();
            this.DefualtDataFormObject.load(this.FormName + "_Form?Default=true");
            this.DefualtDataFormObject.enableLiveValidation(true);
            this.setSpecificFormSettingsoNLoad();
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
            var DefaultForData = this.DefualtDataFormObject.getValues();
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                DefaultForData["ORDER_DATE=DATE"] = this.DefualtDataFormObject.getItemValue("ORDER_DATE=DATE", true);
                DefaultForData["DELIVERY_DATE=DATE"] = this.DefualtDataFormObject.getItemValue("DELIVERY_DATE=DATE", true);
            }
            this.DataEntryLayoutCell.progressOn();
            var Response = SynchronousGetAjaxRequest("saveUpdateDefaultFormValue?VALUE=" + JSON.stringify(DefaultForData) + "&MODULE=" + module_name + "&KEY=" + key_name, "", null);
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
            this.GlobalFormJSONValues = this.FormObject.getValues();
            this.setSpecificBeforeSave();
            if (this.FormObject.validate()) {
                if (this.customValidation()) {
                    this.FormObject.updateValues();
                    //this.DataEntryLayoutCell.progressOn();
                    var Response = SynchronousGetAjaxRequest(this.FormName + "?ParamData=" + JSON.stringify(this.GlobalFormJSONValues), "", null);
                    if (Response.RESPONSE_STATUS === "SUCCESS") {
                        showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.setFormStateAfterSave();
                        this.NotificationCell.collapse();
                        this.DataEntryLayoutCell.progressOff();
                    }
                    if (Response.RESPONSE_STATUS === "FAILED") {
                        showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.attachHTMLString(this.FormName + "  : <b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                        this.NotificationCell.expand();
                        //progressOffCustom(this.DataEntryLayoutCell);
                    }
                }
            }
            else {
                showFailedNotification("Data Validation Error")
            }
        }
        public customValidation() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                if (this.FormObject.getItemValue("DELIVERY_DATE=DATE").getTime() < this.FormObject.getItemValue("ORDER_DATE=DATE").getTime()) {
                    showFailedNotification("Delivery date must be after or equal of order date");
                    return false;
                }
                if(parseInt(this.FormObject.getItemValue("ADVANCE=NUM")) > parseInt(this.FormObject.getItemValue("PRICE=NUM"))){
                  showFailedNotification("Advance must be less or equal than price.");
                  return false;
                }
            }
            return true;

        }
        public setSpecificFormSettingsoNLoad() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                this.FormObject.attachEvent("onChange", (name) => {
                    if (name === "CUSTOM_RATE=NUM") {
                        if (this.FormObject.getItemValue("CUSTOM_RATE=NUM") === 1) {
                            this.customRateWindow();
                            this.FormObject.disableItem("ITEM_BUTTON=BUTTON");
                        }
                        else {
                            this.FormObject.enableItem("ITEM_BUTTON=BUTTON");
                        }

                    }
                });
                //this.FormObject.attachEvent("onXLE", () => {
                this.FormObject.attachEvent("onButtonClick", (name) => {
                    if (name === "ITEM_BUTTON=BUTTON")
                        var Value = this.constructItemSelectionWindow();
                });
                //  });
            }
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_USER) {
                this.OperationToolbar.attachEvent("onXLE", () => {
                    this.OperationToolbar.disableItem("save_default");
                    this.OperationToolbar.disableItem("default");
                });
                this.DataTabber.tabs("default").disable();
            }
        }
        public setSpecificBeforeSave() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                if (this.FormObject.getItemValue("CUSTOM_RATE=NUM") === 1) {
                    this.GlobalFormJSONValues["ITEM_DATA"] = this.CustomRate;
                }
                else {
                    this.GlobalFormJSONValues["ITEM_DATA"] = this.SelectedItemNameList;
                }
                this.GlobalFormJSONValues["ORDER_DATE=DATE"] = this.FormObject.getItemValue("ORDER_DATE=DATE", true);
                this.GlobalFormJSONValues["DELIVERY_DATE=DATE"] = this.FormObject.getItemValue("DELIVERY_DATE=DATE", true);
            }
        }
        public setSpecificAfterSave() {
            if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {

            }
        }
        public constructItemSelectionWindow() {
            this.SelectedItemNameList = [];
            var ItemWindow = this.MainUtilityObj.getModelWindow("Select Items", 535, 500);
            ItemWindow.progressOn();
            var ItemGrid = ItemWindow.attachGrid();
            ItemGrid.load("getExtraItems?ITEM_TYPE=" + this.FormObject.getItemValue("ORDER_TYPE=STR"));
            ItemGrid.setNoHeader(true)
            ItemGrid.attachEvent("onXLE", () => {
                ItemWindow.progressOff();
            });
            var ToolBar = ItemWindow.attachToolbar();
            ToolBar.addButton("OK", 1, "OK", "resources/Images/ok.png", "resources/Images/ok.png");
            ToolBar.setAlign("right");
            ToolBar.attachEvent("onClick", (id) => {
                if (id === "OK") {
                    ItemGrid.forEachRow((id) => {
                        if (ItemGrid.cells(id, 2).getValue() != "") {
                            this.SelectedItemNameList.push(ItemGrid.getUserData(id, "ITEM_NAME"));
                        }
                    });
                    ItemWindow.close();
                }
            });
            ItemGrid.attachEvent("onRowSelect", (id_1, ind) => {
                if (ind === 1) {
                    var PARENT_TYPE = ItemGrid.getUserData(id_1, "PARENT_TYPE");
                    if (ItemGrid.cells(id_1, 2).getValue() != "") {
                        ItemGrid.cells(id_1, 2).setValue("");
                    }
                    else {
                        ItemGrid.cells(id_1, 2).setValue("<img src='resources/Images/ok.png' width='30px' height='30px'/>");
                    }
                    ItemGrid.forEachRow((id) => {
                        if (ItemGrid.getUserData(id, "PARENT_TYPE") === PARENT_TYPE && id_1 != id) {
                            ItemGrid.cells(id, 2).setValue("");
                        }
                    });
                }
            });
        }
        public customRateWindow() {
            var FormObj = [
                { type: "settings", offsetLeft: "15", position: "label-left", labelWidth: 90, inputWidth: 130 },
                { type: "label", label: "<span style=\'color:white;\'>MASTER AND TAILOR RATE</span>", name: "MASTERANDTAILORRATE", labelWidth: "220", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label" },
                { type: "input", label: "MASTER RATE", name: "MASTER_RATE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", validate: "isValidNumeric", required: true, maxLength: "3", value: "0" },
                { type: "input", label: "TAILOR RATE ", name: "TAILOR_RATE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", validate: "isValidNumeric", required: true, maxLength: "3", value: "0" },
                { type: "button", name: "OK=BUTTON", value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>OK</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Ok" }
            ];
            var ItemWindow = this.MainUtilityObj.getModelWindow("Enter Custom Rates", 290, 300);
            var RateForm = ItemWindow.attachForm(FormObj);
            RateForm.setFocusOnFirstActive();
            RateForm.keyPlus();
            RateForm.attachEvent("onButtonClick", (name) => {
                this.CustomRate = RateForm.getValues();
                ItemWindow.close();
            });
        }
    }
}
