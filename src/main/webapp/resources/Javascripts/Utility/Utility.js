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
                    this.isDefaultOn = false;
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
                    this.ModifiedLayoutObject.cells("b").progressOn();
                    this.DataTabber = this.ModifiedLayoutObject.cells("a").attachTabbar();
                    this.DataTabber.addTab("data", "Data Entry", null, null, true, false);
                    this.DataTabber.addTab("default", "Set Default Data", null, null, false, false);
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("operationToolbar", "json");
                    this.OperationToolbar.attachEvent("onClick", function (id) {
                        if (id === "clear") {
                            _this.FormObject.clear();
                        }
                        if (id === "new") {
                            _this.setFormStateNewRecord();
                        }
                        if (id === "save") {
                            _this.validateAndSaveFormData();
                        }
                        if (id === "save_default") {
                            _this.saveDefualtFormValue("FORM_DEFAULT_VALUE", _this.FormName);
                        }
                    });
                    this.OperationToolbar.attachEvent("onStateChange", function (id, state) {
                        if (id === "default") {
                            _this.isDefaultOn = state;
                            _this.FormInitialization();
                        }
                    });
                    this.FormInitialization();
                    this.FormObject.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                    this.DataViewGridObject = this.ModifiedLayoutObject.cells("b").attachGrid();
                    this.DataViewGridObject.load("LoadDataViewGrid?gridname=" + this.FormName);
                    this.DataViewGridObject.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                    });
                    this.DataViewGridObject.attachEvent("onRowSelect", function (id, ind) {
                    });
                };
                FormEntryManager.prototype.FormInitialization = function () {
                    if (this.FormObject != null || this.FormObject != undefined) {
                        this.FormObject.unload();
                    }
                    this.FormObject = this.DataTabber.tabs("data").attachForm();
                    this.FormObject.load(this.FormName + "_Form?Default=" + this.isDefaultOn);
                    this.FormObject.enableLiveValidation(true);
                    if (this.DefualtDataFormObject != null || this.DefualtDataFormObject != undefined) {
                        this.DefualtDataFormObject.unload();
                    }
                    this.DefualtDataFormObject = this.DataTabber.tabs("default").attachForm();
                    this.DefualtDataFormObject.load(this.FormName + "_Form?Default=true");
                    this.DefualtDataFormObject.enableLiveValidation(true);
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
                FormEntryManager.prototype.saveDefualtFormValue = function (module_name, key_name) {
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
                };
                FormEntryManager.prototype.validateAndSaveFormData = function () {
                    this.setSpecificBeforeSave();
                    if (this.FormObject.validate()) {
                        this.FormObject.updateValues();
                        this.DataEntryLayoutCell.progressOn();
                        var Response = SynchronousGetAjaxRequest(this.FormName + "?ParamData=" + JSON.stringify(this.FormObject.getValues()), "", null);
                        if (Response.RESPONSE_STATUS === "SUCCESS") {
                            showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                            this.setFormStateAfterSave();
                            this.NotificationCell.collapse();
                            this.DataEntryLayoutCell.progressOff();
                        }
                        if (Response.RESPONSE_STATUS === "FAILED") {
                            showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                            this.NotificationCell.attachHTMLString(this.FormName + "<b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                            this.NotificationCell.expand();
                            progressOffCustom(this.DataEntryLayoutCell);
                        }
                    }
                    else {
                        showFailedNotification("Data Validation Error");
                    }
                };
                FormEntryManager.prototype.setSpecificFormSettingsoNLoad = function () {
                    var _this = this;
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ITEM) {
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_USER) {
                        this.OperationToolbar.attachEvent("onXLE", function () {
                            _this.OperationToolbar.disableItem("save_default");
                            _this.OperationToolbar.disableItem("default");
                        });
                        this.DataTabber.tabs("default").disable();
                    }
                };
                FormEntryManager.prototype.setSpecificBeforeSave = function () {
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ITEM) {
                    }
                };
                FormEntryManager.prototype.setSpecificAfterSave = function () {
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ITEM) {
                    }
                };
                return FormEntryManager;
            }());
            utilty.FormEntryManager = FormEntryManager;
        })(utilty = ordermanager.utilty || (ordermanager.utilty = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
