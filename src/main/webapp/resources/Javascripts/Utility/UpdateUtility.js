var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var UpdateUtility;
        (function (UpdateUtility_1) {
            var UpdateUtility = (function () {
                function UpdateUtility(UpdateModuleName, LayoutCell, NotificationCell, QueryFormHeight) {
                    this.UpdateModuleName = UpdateModuleName;
                    this.LayoutCell = LayoutCell;
                    this.NotificationCell = NotificationCell;
                    this.QueryFormHeight = QueryFormHeight;
                    this.constructLayout();
                    this.constructQueryForm();
                    this.shortCutRegister();
                }
                UpdateUtility.prototype.constructLayout = function () {
                    this.ModifiedLayoutObject = this.LayoutCell.attachLayout({
                        pattern: "2E",
                        cells: [
                            { id: "a", text: "Query", header: false, height: this.QueryFormHeight },
                            { id: "b", text: "UpdateForm", header: false, }
                        ]
                    });
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("operationToolbarUpdate?formname=" + this.UpdateModuleName, "json");
                    this.OperationToolbar.attachEvent("onClick", function (id) {
                        if (id === "update") {
                        }
                        if (id === "clear") {
                        }
                        if (id === "save_default") {
                        }
                    });
                };
                UpdateUtility.prototype.shortCutRegister = function () {
                    var _this = this;
                    shortcut.add("F4", function () {
                        _this.FormObject.lock();
                        _this.QueryForm.setFocusOnFirstActive();
                        _this.QueryForm.setItemValue("BILL_NO=STR", "");
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.add("F8", function () {
                        _this.validateAndSaveFormData();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                };
                UpdateUtility.prototype.constructQueryForm = function () {
                    var _this = this;
                    if (this.QueryForm != null || this.QueryForm != undefined) {
                        this.QueryForm.unload();
                    }
                    this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
                    this.QueryForm.load(this.UpdateModuleName + "_QueryForm");
                    this.QueryForm.attachEvent("onEnter", function () {
                        _this.constructUpdateForm();
                    });
                    this.QueryForm.attachEvent("onXLE", function () {
                        _this.QueryForm.setFocusOnFirstActive();
                    });
                };
                UpdateUtility.prototype.constructUpdateForm = function () {
                    var _this = this;
                    this.ModifiedLayoutObject.cells("b").progressOn();
                    if (this.FormObject != null || this.FormObject != undefined) {
                        this.FormObject.unload();
                    }
                    var ParameterValue = this.QueryForm.getValues();
                    this.FormObject = this.ModifiedLayoutObject.cells("b").attachForm();
                    this.FormObject.load(this.UpdateModuleName + "_Form?ParamJson=" + JSON.stringify(ParameterValue));
                    this.FormObject.enableLiveValidation(true);
                    this.FormObject.attachEvent("onXLE", function () {
                        _this.FormObject.setFocusOnFirstActive();
                        _this.FormObject.keyPlus();
                        progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                        _this.setSpecificFormSettingsoNLoad();
                    });
                    this.FormObject.attachEvent("onButtonClick", function (name) {
                        if (name === "ITEM_BUTTON=BUTTON")
                            var Value = _this.constructItemSelectionWindow();
                    });
                };
                UpdateUtility.prototype.constructItemSelectionWindow = function () {
                    var _this = this;
                    this.SelectedItemNameList = [];
                    var ItemWindow = new com.ordermanager.utilty.MainUtility().getModelWindow("Select Items", 535, 500);
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
                UpdateUtility.prototype.customRateWindow = function () {
                    var _this = this;
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
                    RateForm.attachEvent("onButtonClick", function (name) {
                        _this.CustomRate = RateForm.getValues();
                        ItemWindow.close();
                    });
                };
                UpdateUtility.prototype.setSpecificFormSettingsoNLoad = function () {
                    var _this = this;
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
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
                        this.FormObject.attachEvent("onButtonClick", function (name) {
                            if (name === "ITEM_BUTTON=BUTTON")
                                var Value = _this.constructItemSelectionWindow();
                        });
                        this.FormObject.attachEvent("onChange", function (name, value) {
                            if (name == "ORDER_STATUS=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("CURRENT_LOCATION=STR"), "CURRENT_LOCATIONS", "LOCATION_NAME", "PARENT_STATUS", value);
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("ORDER_SUB_STATUS=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_PARENT_NAME", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                    }
                };
                UpdateUtility.prototype.validateAndSaveFormData = function () {
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
                        showFailedNotification("Data Validation Error");
                    }
                };
                UpdateUtility.prototype.setSpecificAfterSave = function () {
                    this.OperationToolbar.disableItem("update");
                    shortcut.remove("F8");
                };
                UpdateUtility.prototype.setSpecificBeforeSave = function () {
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
                        if (this.FormObject.getItemValue("CUSTOM_RATE=NUM") === 1) {
                            this.GlobalFormJSONValues["ITEM_DATA"] = this.CustomRate;
                        }
                        else {
                            this.GlobalFormJSONValues["ITEM_DATA"] = this.SelectedItemNameList;
                        }
                        this.GlobalFormJSONValues["DELIVERY_DATE=DATE"] = this.FormObject.getItemValue("DELIVERY_DATE=DATE", true);
                    }
                };
                UpdateUtility.prototype.customValidation = function () {
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_NEW_ORDER) {
                        if (this.FormObject.getItemValue("DELIVERY_DATE=DATE").getTime() < this.FormObject.getItemValue("ORDER_DATE=DATE").getTime()) {
                            showFailedNotification("Delivery date must be after or equal of order date");
                            return false;
                        }
                        if (parseInt(this.FormObject.getItemValue("ADVANCE=NUM")) > parseInt(this.FormObject.getItemValue("PRICE=NUM"))) {
                            showFailedNotification("Advance must be less or equal than price.");
                            return false;
                        }
                    }
                    return true;
                };
                return UpdateUtility;
            }());
            UpdateUtility_1.UpdateUtility = UpdateUtility;
        })(UpdateUtility = ordermanager.UpdateUtility || (ordermanager.UpdateUtility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
