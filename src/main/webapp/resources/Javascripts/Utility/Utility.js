/// <reference path="../Home/OrderManagerHome.ts"/>
var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var utilty;
        (function (utilty) {
            var MainUtility = /** @class */ (function () {
                function MainUtility() {
                }
                MainUtility.getRandomColorLight = function () {
                    var letters = '0123456789ABCDEF';
                    var color = '#';
                    for (var i = 0; i < 6; i++) {
                        color += letters[Math.floor(Math.random() * 16)];
                    }
                    return color;
                };
                MainUtility.getRandomColorDark = function () {
                    var letters = '012345'.split('');
                    var color = '#';
                    color += letters[Math.round(Math.random() * 5)];
                    letters = '0123456789ABCDEF'.split('');
                    for (var i = 0; i < 5; i++) {
                        color += letters[Math.round(Math.random() * 15)];
                    }
                    return color;
                };
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
                MainUtility.getModelWindowImageViewer = function (HeaderText, Height, Width) {
                    var myWins = new dhtmlXWindows();
                    myWins.createWindow("win1", 50, 50, Height, Width);
                    myWins.window("win1").center();
                    myWins.window("win1").setText(HeaderText);
                    return myWins.window("win1");
                };
                MainUtility.getModelWindow = function (HeaderText, Height, Width) {
                    var myWins = new dhtmlXWindows();
                    myWins.createWindow("win1", 50, 50, Height, Width);
                    myWins.window("win1").denyPark();
                    myWins.window("win1").denyResize();
                    myWins.window("win1").center();
                    myWins.window("win1").setModal(true);
                    myWins.window("win1").setText(HeaderText);
                    shortcut.add("Esc", function () {
                        myWins.window("win1").close();
                        shortcut.remove("Esc");
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    return myWins.window("win1");
                    ;
                };
                MainUtility.setDynamicSelectBoxOptions = function (TargetSelectObject, TableName, ColumnName, QueryColumn, QueryValue) {
                    var Params = "?TableName=" + TableName + "&ColumnName=" + ColumnName + "&QueryColumn=" + QueryColumn + "&QueryValue=" + QueryValue;
                    var Response = SynchronousGetAjaxRequest("getComboValues" + Params, "", null);
                    if (Response.RESPONSE_STATUS === "SUCCESS") {
                        var ArrLength = TargetSelectObject.length;
                        for (var i = 0; i < ArrLength; i++) {
                            TargetSelectObject.remove(0);
                        }
                        var OptionsData = JSON.parse(Response.RESPONSE_VALUE.VALUES);
                        for (var i = 0; i < OptionsData.length; i++) {
                            TargetSelectObject.add(new Option(OptionsData[i].text, OptionsData[i].value));
                        }
                    }
                    if (Response.RESPONSE_STATUS === "FAILED") {
                        showFailedNotification(Response.RESPONSE_MESSAGE);
                    }
                };
                MainUtility.showImageViewer = function (Container, Module, Key, GalleryWidth, previewHeight, previewWidth) {
                    var _this = this;
                    var imageViewerLayout = Container.attachLayout({
                        pattern: "2U",
                        cells: [
                            { id: "a", text: "GALLERY", header: false, width: GalleryWidth },
                            { id: "b", text: "IMAGE", header: false }
                        ]
                    });
                    var myDataView = imageViewerLayout.cells("a").attachDataView({
                        container: "data_container1",
                        type: {
                            template: "<img src=#IMAGE# height='100px' width='200px'>",
                            height: 100,
                            width: 200
                        }
                    });
                    myDataView.load("getImage?MODULE=" + Module + "&KEY=" + Key, "json");
                    myDataView.attachEvent("onXLE", function () {
                        var ImageData = myDataView.get(myDataView.first()).IMAGE;
                        imageViewerLayout.cells("b").attachHTMLString("<h4 style='text-align:center;'>" + myDataView.get(myDataView.first()).IMAGE_DATE + "<br><img src=" + ImageData + " height='" + previewHeight + "px' width='" + previewWidth + "px'></h4>");
                        myDataView.attachEvent("onItemClick", function (id, ev, html) {
                            var ImageData = myDataView.get(id).IMAGE;
                            imageViewerLayout.cells("b").attachHTMLString("<h4 style='text-align:center;'>" + myDataView.get(id).IMAGE_DATE + "<br><img src=" + ImageData + " height='" + previewHeight + "px' width='" + previewWidth + "px'></h4>");
                            return true;
                        });
                        myDataView.attachEvent("onItemDblClick", function (id, ev, html) {
                            var ImageData = myDataView.get(id).IMAGE;
                            _this.getModelWindowImageViewer("Detail Image View", 1200, 650).attachHTMLString("<h4 style='text-align:center;'>" + myDataView.get(id).IMAGE_DATE + "<br><img src=" + ImageData + " height='550px' width='1180px'></h4>");
                            return true;
                        });
                    });
                    return myDataView;
                };
                return MainUtility;
            }());
            utilty.MainUtility = MainUtility;
            var FormEntryManager = /** @class */ (function () {
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
                    this.ModifiedLayoutObject.progressOn();
                    if (this.FormObject != null || this.FormObject != undefined) {
                        this.FormObject.unload();
                    }
                    this.FormObject = this.DataTabber.tabs("data").attachForm();
                    this.FormObject.load(this.FormName + "_Form?Default=" + this.isDefaultOn);
                    this.FormObject.enableLiveValidation(true);
                    this.FormObject.attachEvent("onXLE", function () {
                        _this.FormObject.setFocusOnFirstActive();
                        _this.FormObject.keyPlus();
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                    if (this.DefualtDataFormObject != null || this.DefualtDataFormObject != undefined) {
                        this.DefualtDataFormObject.unload();
                    }
                    this.DefualtDataFormObject = this.DataTabber.tabs("default").attachForm();
                    this.DefualtDataFormObject.load(this.FormName + "_Form?Default=true");
                    this.DefualtDataFormObject.enableLiveValidation(true);
                    this.setSpecificFormSettingsoNLoad();
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
                    var DefaultForData = this.DefualtDataFormObject.getValues();
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                        DefaultForData["ORDER_DATE=DATE"] = this.DefualtDataFormObject.getItemValue("ORDER_DATE=DATE", true);
                        DefaultForData["DELIVERY_DATE=DATE"] = this.DefualtDataFormObject.getItemValue("DELIVERY_DATE=DATE", true);
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_ADD_NEW_ACCOUNT_TRANSACTION) {
                        DefaultForData["TR_DATE=DATE"] = this.DefualtDataFormObject.getItemValue("TR_DATE=DATE", true);
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
                };
                FormEntryManager.prototype.validateAndSaveFormData = function () {
                    this.GlobalFormJSONValues = this.FormObject.getValues();
                    this.setSpecificBeforeSave();
                    if (this.FormObject.validate()) {
                        if (this.customValidation()) {
                            this.FormObject.updateValues();
                            this.DataEntryLayoutCell.progressOn();
                            var Response = SynchronousGetAjaxRequest(this.FormName + "?ParamData=" + JSON.stringify(this.GlobalFormJSONValues), "", null);
                            if (Response.RESPONSE_STATUS === "SUCCESS") {
                                showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                                this.setFormStateAfterSave();
                                this.setSpecificAfterSave();
                                this.NotificationCell.collapse();
                                this.DataEntryLayoutCell.progressOff();
                            }
                            if (Response.RESPONSE_STATUS === "FAILED") {
                                showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                                this.NotificationCell.attachHTMLString(this.FormName + "  : <b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                                this.NotificationCell.expand();
                                progressOffCustom(this.DataEntryLayoutCell);
                            }
                        }
                    }
                    else {
                        showFailedNotification("Data Validation Error");
                    }
                };
                FormEntryManager.prototype.customValidation = function () {
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                        if (this.FormObject.getItemValue("DELIVERY_DATE=DATE").getTime() < this.FormObject.getItemValue("ORDER_DATE=DATE").getTime()) {
                            showFailedNotification("Delivery date must be after or equal of order date");
                            return false;
                        }
                        if (parseInt(this.FormObject.getItemValue("ADVANCE=NUM")) > parseInt(this.FormObject.getItemValue("PRICE=NUM"))) {
                            showFailedNotification("Advance must be less or equal than price.");
                            return false;
                        }
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE) {
                        var Value1 = this.FormObject.getItemValue("STATUS_NAME=STR");
                        var Value2 = this.FormObject.getItemValue("STATUS_PARENT_NAME=STR");
                        if (isCompositeValueUnique("ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_PARENT_NAME", Value1, Value2) == false) {
                            return false;
                        }
                    }
                    return true;
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
                        //this.FormObject.attachEvent("onXLE", () => {
                        this.FormObject.attachEvent("onButtonClick", function (name) {
                            if (name === "ITEM_BUTTON=BUTTON") {
                                var Value = _this.constructItemSelectionWindow();
                            }
                            if (name === "AUTO_BILL=BUTTON") {
                                var BillNo = SynchronousGetAjaxRequest("getNextBillNo");
                                _this.FormObject.setItemValue("BILL_NO=STR", BillNo.toString());
                                _this.FormObject.setItemFocus("VERIFY_BILL_NO=STR");
                            }
                        });
                        //  });
                        this.FormObject.attachEvent("onChange", function (name, value) {
                            if (name == "ORDER_STATUS=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("CURRENT_LOCATION=STR"), "CURRENT_LOCATIONS", "LOCATION_NAME", "PARENT_STATUS", value);
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("ORDER_SUB_STATUS=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_PARENT_NAME", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                        this.DefualtDataFormObject.attachEvent("onChange", function (name, value) {
                            if (name == "ORDER_STATUS=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.DefualtDataFormObject.getOptions("CURRENT_LOCATION=STR"), "CURRENT_LOCATIONS", "LOCATION_NAME", "PARENT_STATUS", value);
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.DefualtDataFormObject.getOptions("ORDER_SUB_STATUS=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_PARENT_NAME", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_ADD_NEW_ACCOUNT_TRANSACTION) {
                        this.FormObject.attachEvent("onXLE", function () {
                            //  this.FormObject.setItemValue("TR_DATE=DATE", getCurrentDate());
                        });
                        this.FormObject.attachEvent("onChange", function (name, value) {
                            if (name == "TR_MODULE=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("ACCOUNT_NAME=STR"), "ACCOUNT_REGISTER", "ACCOUNT_NAME", "ACCOUNT_MODULE", value);
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("MODULE_TRANSACTION_TYPE=STR"), "ACCOUNT_BOOK_SUBTYPES", "ACCOUNT_SUBTYPE", "ACCOUNT_MODULE", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                        this.DefualtDataFormObject.attachEvent("onChange", function (name, value) {
                            if (name == "TR_MODULE=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.DefualtDataFormObject.getOptions("ACCOUNT_NAME=STR"), "ACCOUNT_REGISTER", "ACCOUNT_NAME", "ACCOUNT_MODULE", value);
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.DefualtDataFormObject.getOptions("MODULE_TRANSACTION_TYPE=STR"), "ACCOUNT_BOOK_SUBTYPES", "ACCOUNT_SUBTYPE", "ACCOUNT_MODULE", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_USER) {
                        this.OperationToolbar.attachEvent("onXLE", function () {
                            _this.OperationToolbar.disableItem("save_default");
                            _this.OperationToolbar.disableItem("default");
                        });
                        this.DataTabber.tabs("default").disable();
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE) {
                        this.FormObject.attachEvent("onXLE", function () {
                            _this.ModifiedLayoutObject.progressOn();
                            com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("STATUS_PARENT_NAME=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_TYPE", "MAIN_STATUS");
                            progressOffCustom(_this.ModifiedLayoutObject);
                        });
                        this.FormObject.attachEvent("onChange", function (name, value) {
                            if (name == "STATUS_TYPE=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.FormObject.getOptions("STATUS_PARENT_NAME=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_TYPE", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                        this.DefualtDataFormObject.attachEvent("onXLE", function () {
                            _this.ModifiedLayoutObject.progressOn();
                            com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.DefualtDataFormObject.getOptions("STATUS_PARENT_NAME=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_TYPE", "MAIN_STATUS");
                            progressOffCustom(_this.ModifiedLayoutObject);
                        });
                        this.DefualtDataFormObject.attachEvent("onChange", function (name, value) {
                            if (name == "STATUS_TYPE=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.DefualtDataFormObject.getOptions("STATUS_PARENT_NAME=STR"), "ORDER_STATUS_TYPES", "STATUS_NAME", "STATUS_TYPE", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
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
                        this.GlobalFormJSONValues["ORDER_DATE=DATE"] = this.FormObject.getItemValue("ORDER_DATE=DATE", true);
                        this.GlobalFormJSONValues["DELIVERY_DATE=DATE"] = this.FormObject.getItemValue("DELIVERY_DATE=DATE", true);
                    }
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_ADD_NEW_ACCOUNT_TRANSACTION) {
                        this.GlobalFormJSONValues["TR_DATE=DATE"] = this.FormObject.getItemValue("TR_DATE=DATE", true);
                    }
                };
                FormEntryManager.prototype.setSpecificAfterSave = function () {
                    if (this.FormName === com.ordermanager.home.OrderManagerHome.FORM_NEW_ORDER) {
                        var BILL_NO = this.FormObject.getItemValue("BILL_NO=STR");
                        var windowObject = com.ordermanager.utilty.MainUtility.getModelWindow("Take Product Image", 580, 580).attachURL("resources/JS_WEBCAM/Camera.html?KEY=" + BILL_NO + "&MODULE=ORDERS");
                    }
                };
                FormEntryManager.prototype.constructItemSelectionWindow = function () {
                    var _this = this;
                    this.SelectedItemNameList = [];
                    var ItemWindow = this.MainUtilityObj.getModelWindow("Select Items", 535, 500);
                    ItemWindow.progressOn();
                    var ItemGrid = ItemWindow.attachGrid();
                    var BILL_NO = this.FormObject.getItemValue("BILL_NO=STR");
                    ItemGrid.load("getExtraItems?ITEM_TYPE=" + this.FormObject.getItemValue("ORDER_TYPE=STR") + "&BILL_NO=" + BILL_NO);
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
