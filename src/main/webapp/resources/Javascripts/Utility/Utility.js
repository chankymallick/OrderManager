var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var utilty;
        (function (utilty) {
            var MainUtility = (function () {
                function MainUtility() {
                }
                MainUtility.prototype.getModelWindow = function (HeaderText, Height, Width) {
                    var myWins = new dhtmlXWindows();
                    myWins.createWindow("win1", 50, 50, Height, Width);
                    myWins.window("win1").denyPark();
                    myWins.window("win1").denyResize();
                    myWins.window("win1").center();
                    myWins.window("win1").setModal(true);
                    myWins.window("win1").setText(HeaderText);
                    return myWins.window("win1");
                };
                return MainUtility;
            }());
            utilty.MainUtility = MainUtility;
            var FormEntryManager = (function () {
                function FormEntryManager(layoutCell, notificationCell, formname, dataentryCellHeight, dataviewcellheight) {
                    this.isDefaultOn = false;
                    this.GlobalFormJSONValues = {};
                    this.SelectedItemNameList = [];
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
                    this.OperationToolbar.loadStruct("operationToolbar?formname=" + this.FormName, "json");
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
                        _this.FormObject.setFocusOnFirstActive();
                        _this.FormObject.keyPlus();
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
                    var _this = this;
                    if (this.FormObject != null || this.FormObject != undefined) {
                        this.FormObject.unload();
                    }
                    this.FormObject = this.DataTabber.tabs("data").attachForm();
                    this.FormObject.load(this.FormName + "_Form?Default=" + this.isDefaultOn);
                    this.FormObject.enableLiveValidation(true);
                    this.FormObject.attachEvent("onXLE", function () {
                        _this.FormObject.setFocusOnFirstActive();
                    });
                    this.FormObject.keyPlus();
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
                    this.GlobalFormJSONValues = this.FormObject.getValues();
                    this.setSpecificBeforeSave();
                    if (this.FormObject.validate()) {
                        this.FormObject.updateValues();
                        this.DataEntryLayoutCell.progressOn();
                        var Response = SynchronousGetAjaxRequest(this.FormName + "?ParamData=" + JSON.stringify(this.GlobalFormJSONValues), "", null);
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
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                        this.FormObject.attachEvent("onChange", function (name) {
                            if (name === "CUSTOM_RATE=NUM") {
                                if (_this.FormObject.getItemValue("CUSTOM_RATE=NUM") === 1) {
                                    _this.customRateWindow();
                                    _this.FormObject.disableItem("ITEM_BUTTON=BUTTON");
                                }
                                else {
                                    _this.FormObject.enableItem("ITEM_BUTTON=BUTTON");
                                }
                            }
                        });
                        this.FormObject.attachEvent("onXLE", function () {
                            _this.FormObject.attachEvent("onButtonClick", function (name) {
                                if (name === "ITEM_BUTTON=BUTTON")
                                    var Value = _this.constructItemSelectionWindow();
                            });
                        });
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
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                        if (this.FormObject.getItemValue("CUSTOM_RATE=NUM") === 1) {
                            this.GlobalFormJSONValues["ITEM_DATA"] = this.CustomRate;
                        }
                        else {
                            this.GlobalFormJSONValues["ITEM_DATA"] = this.SelectedItemNameList;
                        }
                    }
                };
                FormEntryManager.prototype.setSpecificAfterSave = function () {
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                    }
                };
                FormEntryManager.prototype.constructItemSelectionWindow = function () {
                    var _this = this;
                    this.SelectedItemNameList = [];
                    var ItemWindow = this.MainUtilityObj.getModelWindow("Select Items", 535, 500);
                    ItemWindow.progressOn();
                    var ItemGrid = ItemWindow.attachGrid();
                    ItemGrid.load("getExtraItems?ITEM_TYPE=" + this.FormObject.getItemValue("ORDER_TYPE=STR"));
                    ItemGrid.setNoHeader(true);
                    ItemGrid.attachEvent("onXLE", function () {
                        ItemWindow.progressOff();
                    });
                    var ToolBar = ItemWindow.attachToolbar();
                    ToolBar.addButton("OK", 1, "OK", "resources/Images/ok.png", "resources/Images/ok.png");
                    ToolBar.setAlign("right");
                    ToolBar.attachEvent("onClick", function (id) {
                        if (id === "OK") {
                            ItemGrid.forEachRow(function (id) {
                                if (ItemGrid.cells(id, 2).getValue() != "") {
                                    _this.SelectedItemNameList.push(ItemGrid.getUserData(id, "ITEM_NAME"));
                                }
                            });
                            ItemWindow.close();
                        }
                    });
                    ItemGrid.attachEvent("onRowSelect", function (id_1, ind) {
                        if (ind === 1) {
                            var PARENT_TYPE = ItemGrid.getUserData(id_1, "PARENT_TYPE");
                            if (ItemGrid.cells(id_1, 2).getValue() != "") {
                                ItemGrid.cells(id_1, 2).setValue("");
                            }
                            else {
                                ItemGrid.cells(id_1, 2).setValue("<img src='resources/Images/ok.png' width='30px' height='30px'/>");
                            }
                            ItemGrid.forEachRow(function (id) {
                                if (ItemGrid.getUserData(id, "PARENT_TYPE") === PARENT_TYPE && id_1 != id) {
                                    ItemGrid.cells(id, 2).setValue("");
                                }
                            });
                        }
                    });
                };
                FormEntryManager.prototype.customRateWindow = function () {
                    var _this = this;
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
                    RateForm.attachEvent("onButtonClick", function (name) {
                        _this.CustomRate = RateForm.getValues();
                        ItemWindow.close();
                    });
                };
                return FormEntryManager;
            }());
            utilty.FormEntryManager = FormEntryManager;
        })(utilty = ordermanager.utilty || (ordermanager.utilty = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
