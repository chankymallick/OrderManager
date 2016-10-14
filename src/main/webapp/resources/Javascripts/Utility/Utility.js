var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var utilty;
        (function (utilty) {
            var MainUtility = (function () {
                function MainUtility() {
                }
                return MainUtility;
            }());
            utilty.MainUtility = MainUtility;
            var FormEntryManager = (function () {
                function FormEntryManager(layoutCell, notificationCell, formname, dataentryCellHeight, dataviewcellheight) {
                    this.DataEntryLayoutCellHeight = dataentryCellHeight;
                    this.DataViewLayoutCellHeight = dataviewcellheight;
                    this.DataEntryLayoutCell = layoutCell;
                    this.NotificationCell = notificationCell;
                    this.FormName = formname;
                    this.constructInnerLayoutforDataEntry();
                    this.MainUtilityObj = new com.ordermanager.utilty.MainUtility();
                    this.shortCutRegister();
                }
                FormEntryManager.prototype.shortCutRegister = function () {
                    var _this = this;
                    shortcut.add("F8", function () {
                        _this.validateAndSaveFormData();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                };
                FormEntryManager.prototype.constructInnerLayoutforDataEntry = function () {
                    var _this = this;
                    this.ModifiedLayoutObject = this.DataEntryLayoutCell.attachLayout({
                        pattern: "2E",
                        cells: [
                            { id: "a", text: "Data", header: false },
                            { id: "b", text: "Existing Data", header: false, height: this.DataViewLayoutCellHeight }
                        ]
                    });
                    this.ModifiedLayoutObject.progressOn();
                    this.DataTabber = this.ModifiedLayoutObject.cells("a").attachTabbar();
                    this.DataTabber.addTab("data", "Data Entry", null, null, true, false);
                    this.DataTabber.addTab("default", "Set Default Data", null, null, false, false);
                    this.FormInitialization();
                    this.FormObject.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("operationToolbar", "json");
                    this.DataViewGridObject = this.ModifiedLayoutObject.cells("b").attachGrid();
                    this.DataViewGridObject.load("LoadDataViewGrid?gridname=" + this.FormName);
                    this.DataViewGridObject.attachEvent("onRowSelect", function (id, ind) {
                    });
                };
                FormEntryManager.prototype.FormInitialization = function () {
                    if (this.FormObject != null || this.FormObject != undefined) {
                        this.FormObject.unload();
                    }
                    this.FormObject = this.DataTabber.tabs("data").attachForm();
                    this.FormObject.load(this.FormName);
                    this.FormObject.enableLiveValidation(true);
                };
                FormEntryManager.prototype.setFormStateAfterSave = function () {
                    var _this = this;
                    this.FormObject.lock();
                    this.OperationToolbar.enableItem("new");
                    this.OperationToolbar.disableItem("save");
                    this.OperationToolbar.disableItem("clear");
                    this.DataViewGridObject.load("LoadDataViewGrid?gridname=" + this.FormName);
                    shortcut.add("F4", function () {
                        _this.setFormStateNewRecord();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.remove("F8");
                };
                FormEntryManager.prototype.setFormStateNewRecord = function () {
                    var _this = this;
                    this.FormInitialization();
                    this.OperationToolbar.disableItem("new");
                    this.OperationToolbar.enableItem("save");
                    this.OperationToolbar.enableItem("clear");
                    shortcut.remove("F4");
                    shortcut.add("F8", function () {
                        _this.validateAndSaveFormData();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                };
                FormEntryManager.prototype.validateAndSaveFormData = function () {
                    if (this.FormObject.validate()) {
                        this.FormObject.updateValues();
                        var Response = SynchronousGetAjaxRequest("addItem?ParamData=" + JSON.stringify(this.FormObject.getValues()), "", this.DataEntryLayoutCell);
                        if (Response.RESPONSE_STATUS === "SUCCESS") {
                            showSuccessNotification(Response.RESPONSE_MESSAGE);
                            this.setFormStateAfterSave();
                            this.NotificationCell.collapse();
                        }
                        if (Response.RESPONSE_STATUS === "FAILED") {
                            showFailedNotification(Response.RESPONSE_MESSAGE);
                            this.NotificationCell.attachHTMLString("<b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                            this.NotificationCell.expand();
                        }
                    }
                    else {
                        showFailedNotification("Data Validation Error");
                    }
                };
                FormEntryManager.prototype.setSpecificFormSettingsoNLoad = function () {
                    if (this.FormName === "loadNewOrderItemForm") {
                    }
                };
                FormEntryManager.prototype.setSpecificBeforeSave = function () {
                    if (this.FormName === "loadNewOrderItemForm") {
                    }
                };
                FormEntryManager.prototype.setSpecificAfterSave = function () {
                    if (this.FormName === "loadNewOrderItemForm") {
                    }
                };
                return FormEntryManager;
            }());
            utilty.FormEntryManager = FormEntryManager;
        })(utilty = ordermanager.utilty || (ordermanager.utilty = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
