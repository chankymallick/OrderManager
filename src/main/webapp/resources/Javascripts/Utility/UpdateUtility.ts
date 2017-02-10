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
module com.ordermanager.UpdateUtility {
    export class UpdateUtility {
        public UpdateModuleName: any;
        public LayoutCell: any;
        public QueryFormHeight: any;
        public NotificationCell: any;
        public ModifiedLayoutObject: any;
        public OperationToolbar: any;
        public QueryForm: any;
        public FormObject:any;
        public SelectedItemNameList:any;
        public CustomRate:any;
        public GlobalFormJSONValues:any;

        constructor(UpdateModuleName: any, LayoutCell: any, NotificationCell: any, QueryFormHeight: any) {
            this.UpdateModuleName = UpdateModuleName;
            this.LayoutCell = LayoutCell;
            this.NotificationCell = NotificationCell;
            this.QueryFormHeight = QueryFormHeight;
            this.constructLayout();
            this.constructQueryForm();
            this.shortCutRegister();
        }
        public constructLayout() {
            this.ModifiedLayoutObject = this.LayoutCell.attachLayout({
                pattern: "2E",
                cells: [
                    { id: "a", text: "Query", header: false, height: this.QueryFormHeight },
                    { id: "b", text: "UpdateForm", header: false, }
                ]
            });
            this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
            this.OperationToolbar.loadStruct("operationToolbarUpdate?formname=" + this.UpdateModuleName, "json");
            this.OperationToolbar.attachEvent("onClick", (id) => {
                if (id === "update") {
                }
                if (id === "clear") {
                }
                if (id === "save_default") {
                }

            });
        }
        public shortCutRegister() {
            shortcut.add("F4", () => {
                this.FormObject.lock();
                this.QueryForm.setFocusOnFirstActive();
                this.QueryForm.setItemValue("BILL_NO=STR","");
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document,
                    'propagate': true
                });
                shortcut.add("F8", () => {
                    this.validateAndSaveFormData();
                }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
        }

        public constructQueryForm() {
            if (this.QueryForm != null || this.QueryForm != undefined) {
                this.QueryForm.unload();
            }
            this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
            this.QueryForm.load(this.UpdateModuleName + "_QueryForm");
            this.QueryForm.attachEvent("onEnter", () => {
            this.constructUpdateForm();
            });
            this.QueryForm.attachEvent("onXLE", () => {
            this.QueryForm.setFocusOnFirstActive();
            });

        }

        public constructUpdateForm() {
              this.ModifiedLayoutObject.cells("b").progressOn();
              if (this.FormObject != null || this.FormObject != undefined) {
                  this.FormObject.unload();
              }
              var ParameterValue = this.QueryForm.getValues();
              this.FormObject =this.ModifiedLayoutObject.cells("b").attachForm();
              this.FormObject.load(this.UpdateModuleName + "_Form?ParamJson="+JSON.stringify(ParameterValue));
              this.FormObject.enableLiveValidation(true);
              this.FormObject.attachEvent("onXLE", () => {
                  this.FormObject.setFocusOnFirstActive();
                  this.FormObject.keyPlus();
                  progressOffCustom(this.ModifiedLayoutObject.cells("b"));
                  this.setSpecificFormSettingsoNLoad();
              });
             if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
              this.FormObject.attachEvent("onButtonClick", (name) => {
                  if (name === "ITEM_BUTTON=BUTTON")
                      var Value = this.constructItemSelectionWindow();
              });
            }
        }

        public constructItemSelectionWindow() {
            this.SelectedItemNameList = [];
            var ItemWindow = new com.ordermanager.utilty.MainUtility().getModelWindow("Select Items", 535, 500);
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
            var ItemWindow = new com.ordermanager.utilty.MainUtility().getModelWindow("Enter Custom Rates", 290, 300);
            var RateForm = ItemWindow.attachForm(FormObj);
            RateForm.setFocusOnFirstActive();
            RateForm.keyPlus();
            RateForm.attachEvent("onButtonClick", (name) => {
                this.CustomRate = RateForm.getValues();
                ItemWindow.close();
            });
        }
        public setSpecificFormSettingsoNLoad() {
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
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
                this.FormObject.attachEvent("onChange", (name, value) => {
                    if (name == "ORDER_STATUS=STR") {
                        this.ModifiedLayoutObject.progressOn();
                        com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.FormObject.getOptions("CURRENT_LOCATION=STR"), "CURRENT_LOCATIONS", "LOCATION_NAME", "PARENT_STATUS", value);
                        com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.FormObject.getOptions("ORDER_SUB_STATUS=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_PARENT_NAME", value);
                        progressOffCustom(this.ModifiedLayoutObject);
                    }
                });
            }
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_ADVANCE) {
              this.FormObject.attachEvent("onChange", (name, value) => {
                  if (name == "ORDER_STATUS=STR") {
                      this.ModifiedLayoutObject.progressOn();
                      com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.FormObject.getOptions("CURRENT_LOCATION=STR"), "CURRENT_LOCATIONS", "LOCATION_NAME", "PARENT_STATUS", value);
                      com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.FormObject.getOptions("ORDER_SUB_STATUS=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_PARENT_NAME", value);
                      progressOffCustom(this.ModifiedLayoutObject);
                  }
              });

            }

        }
        public validateAndSaveFormData() {
            this.GlobalFormJSONValues = this.FormObject.getValues();
            this.setSpecificBeforeSave();
            if (this.FormObject.validate()) {
                if (this.customValidation()) {
                    this.FormObject.updateValues();
                  this.ModifiedLayoutObject.progressOn();
                    var Response = SynchronousGetAjaxRequest(this.UpdateModuleName + "?ParamData=" + JSON.stringify(this.GlobalFormJSONValues), "", null);
                    if (Response.RESPONSE_STATUS === "SUCCESS") {
                        showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.FormObject.lock();
                        this.NotificationCell.collapse();
                        progressOffCustom(this.ModifiedLayoutObject);
                        this.setSpecificAfterSave();
                    }
                    if (Response.RESPONSE_STATUS === "FAILED") {
                        showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.attachHTMLString(this.UpdateModuleName + "  : <b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                        this.NotificationCell.expand();
                        progressOffCustom(this.ModifiedLayoutObject);
                    }
                }
            }
            else {
                showFailedNotification("Data Validation Error")
            }
        }
        public setSpecificAfterSave(){
            this.OperationToolbar.disableItem("update");
            shortcut.remove("F8");
        }
        public setSpecificBeforeSave() {
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
                if (this.FormObject.getItemValue("CUSTOM_RATE=NUM") === 1) {
                    this.GlobalFormJSONValues["ITEM_DATA"] = this.CustomRate;
                }
                else {
                    this.GlobalFormJSONValues["ITEM_DATA"] = this.SelectedItemNameList;
                }
                this.GlobalFormJSONValues["DELIVERY_DATE=DATE"] = this.FormObject.getItemValue("DELIVERY_DATE=DATE", true);
            }
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_ADVANCE) {
              var TempJSON = [];
            
            }
        }
        public customValidation() {
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
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
    }
}
