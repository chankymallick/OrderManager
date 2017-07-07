var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var bulkupdate;
        (function (bulkupdate) {
            var BulkUpdate = (function () {
                function BulkUpdate(UpdateModuleName, LayoutCell, NotificationCell, QueryFormHeight, AssignmentGridColumns) {
                    this.ParameterJSON = {};
                    this.AssigmentStatus = "START";
                    this.UpdateModuleName = UpdateModuleName;
                    this.LayoutCell = LayoutCell;
                    this.NotificationCell = NotificationCell;
                    this.QueryFormHeight = QueryFormHeight;
                    this.constructLayout();
                    this.constructQueryForm();
                    this.constructAssignmentGrid();
                    this.shortCutRegister();
                }
                BulkUpdate.prototype.shortCutRegister = function () {
                    var _this = this;
                    shortcut.add("F8", function () {
                        _this.sendBulkQueryForUpdate();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                };
                BulkUpdate.prototype.constructLayout = function () {
                    this.ModifiedLayoutObject = this.LayoutCell.attachLayout({
                        pattern: "3T",
                        cells: [
                            { id: "a", text: "Query", header: false, height: this.QueryFormHeight },
                            { id: "b", text: "Assigned Orders", header: true, width: 1000 },
                            { id: "c", text: "Statistics", header: true }
                        ]
                    });
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("operationToolbarBulkUpdate?formname=" + this.UpdateModuleName, "json");
                    this.OperationToolbar.attachEvent("onClick", function (id) {
                        if (id === "update") {
                        }
                        if (id === "clear") {
                        }
                        if (id === "save_default") {
                        }
                    });
                };
                BulkUpdate.prototype.constructQueryForm = function () {
                    var _this = this;
                    this.ModifiedLayoutObject.progressOn();
                    if (this.QueryForm != null || this.QueryForm != undefined) {
                        this.QueryForm.unload();
                    }
                    this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
                    this.QueryForm.load(this.UpdateModuleName + "_QueryForm");
                    this.QueryForm.attachEvent("onEnter", function () {
                        var ParameterValue = _this.QueryForm.getValues();
                        _this.loadAssignmentGridData(ParameterValue);
                    });
                    this.QueryForm.attachEvent("onXLE", function () {
                        _this.QueryForm.setFocusOnFirstActive();
                        _this.setSpecificOnLoad();
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                };
                BulkUpdate.prototype.showAlertBox = function (Message) {
                    dhtmlx.message({
                        type: "alert-error",
                        text: Message
                    });
                };
                BulkUpdate.prototype.constructAssignmentGrid = function () {
                    this.AssignmentGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
                    this.AssignmentGrid.load("LoadAssignmentGrid?gridname=" + this.UpdateModuleName + "&ParamJson=");
                    this.AssignmentGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
                    this.attachEventonRow();
                    this.statisticCounterConstruction();
                };
                BulkUpdate.prototype.attachEventonRow = function () {
                    var _this = this;
                    var Removeindex;
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                        Removeindex = 10;
                    }
                    else if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER) {
                        Removeindex = 8;
                    }
                    this.AssignmentGrid.attachEvent("onRowSelect", function (id, index) {
                        if (index === Removeindex && _this.AssigmentStatus == "START") {
                            dhtmlx.confirm({
                                type: "confirm",
                                text: "Do you want to remove selected Order?",
                                callback: function (result) {
                                    if (result) {
                                        _this.AssignmentGrid.deleteRow(id);
                                        _this.calculateStatistics();
                                    }
                                }
                            });
                        }
                        else if (index === Removeindex && _this.AssigmentStatus == "END") {
                            _this.showAlertBox(_this.AssignmentGrid.getUserData(id, "SERVER_DATA"));
                        }
                    });
                };
                BulkUpdate.prototype.loadAssignmentGridData = function (Parameters) {
                    this.ModifiedLayoutObject.progressOn();
                    var GridData;
                    var Response = SynchronousGetAjaxRequest(this.UpdateModuleName + "_Query?ParamData=" + JSON.stringify(Parameters), "", null);
                    if (Response.RESPONSE_STATUS === "SUCCESS") {
                        var GridData = Response.RESPONSE_VALUE.DATA.split(",");
                        var RowNum = parseInt(this.AssignmentGrid.getRowsNum()) + 1;
                        GridData.splice(0, 0, RowNum);
                        if (this.isBillNoExistInAssignmentGrid(GridData[1])) {
                            this.AssignmentGrid.addRow(RowNum, GridData);
                            this.afterAddrow(GridData, RowNum);
                            this.calculateStatistics();
                            this.QueryForm.setItemValue("BILL_NO=STR", "");
                            showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                        }
                        this.NotificationCell.collapse();
                    }
                    if (Response.RESPONSE_STATUS === "FAILED") {
                        showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.attachHTMLString(":<b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                        this.NotificationCell.expand();
                    }
                    progressOffCustom(this.ModifiedLayoutObject);
                };
                BulkUpdate.prototype.afterAddrow = function (GridData, RowNum) {
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                        if (GridData[6] > 10) {
                            this.AssignmentGrid.setRowColor(RowNum, "#43fc2a");
                        }
                        else if (GridData[6] < 0) {
                            this.AssignmentGrid.setRowColor(RowNum, "#ff0000");
                        }
                        else if (GridData[6] < 1) {
                            this.AssignmentGrid.setRowColor(RowNum, "#ff4800");
                        }
                        else if (GridData[6] < 3) {
                            this.AssignmentGrid.setRowColor(RowNum, "#ff7621");
                        }
                        else if (GridData[6] < 5) {
                            this.AssignmentGrid.setRowColor(RowNum, "#effc5f");
                        }
                        else if (GridData[6] <= 10) {
                            this.AssignmentGrid.setRowColor(RowNum, "#a7ff84");
                        }
                    }
                };
                BulkUpdate.prototype.sendBulkQueryForUpdate = function () {
                    this.setSpecificBeforeSave();
                    if (this.QueryForm.validate()) {
                        this.ModifiedLayoutObject.progressOn();
                        var Response = SynchronousGetAjaxRequest(this.UpdateModuleName + "_BulkUpdate" + "?ParamData=" + JSON.stringify(this.ParameterJSON), "", null);
                        if (Response.RESPONSE_STATUS === "SUCCESS") {
                            showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                            this.setFormStateAfterSave(Response);
                            this.NotificationCell.collapse();
                            progressOffCustom(this.ModifiedLayoutObject);
                        }
                    }
                };
                BulkUpdate.prototype.isBillNoExistInAssignmentGrid = function (BillNo) {
                    var _this = this;
                    var status = true;
                    if (this.AssignmentGrid.getRowsNum() == 0) {
                        status = true;
                    }
                    this.AssignmentGrid.forEachRow(function (id) {
                        if (_this.AssignmentGrid.cells(id, 1).getValue() === BillNo) {
                            showFailedNotificationWithICON("Bill no already added in List");
                            status = false;
                        }
                    });
                    return status;
                };
                BulkUpdate.prototype.statisticCounterConstruction = function () {
                    var StatFields = ["TOTAL PIECE", "URGENT PIECES", "EXTRA ITEMS", "STAT1", "STAT2"];
                    this.StatisticsGrid = this.ModifiedLayoutObject.cells("c").attachGrid();
                    this.StatisticsGrid.setHeader("Stats");
                    this.StatisticsGrid.setNoHeader(true);
                    this.StatisticsGrid.setColAlign("left");
                    this.StatisticsGrid.setColTypes("ro");
                    this.StatisticsGrid.init();
                    this.StatisticsGrid.addRow("STAT_KEY1_NAME", StatFields[0]);
                    this.StatisticsGrid.addRow("STAT_KEY1_VALUE", "0");
                    this.StatisticsGrid.addRow("STAT_KEY2_NAME", StatFields[1]);
                    this.StatisticsGrid.addRow("STAT_KEY2_VALUE", "0");
                    this.StatisticsGrid.addRow("STAT_KEY3_NAME", StatFields[2]);
                    this.StatisticsGrid.addRow("STAT_KEY3_VALUE", "0");
                    this.StatisticsGrid.addRow("STAT_KEY4_NAME", StatFields[3]);
                    this.StatisticsGrid.addRow("STAT_KEY4_VALUE", "0");
                    this.StatisticsGrid.addRow("STAT_KEY5_NAME", StatFields[4]);
                    this.StatisticsGrid.addRow("STAT_KEY5_VALUE", "0");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY1_NAME", "color:white;background-color: #00a0ea; font-weight:bold; ");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY2_NAME", "color:white;background-color: #494949; font-weight:bold; ");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY3_NAME", "color:white;background-color: #e26600; font-weight:bold; ");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY4_NAME", "color:white;background-color: #009e98; font-weight:bold; ");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY5_NAME", "color:white;background-color: #0010a0; font-weight:bold; ");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY1_VALUE", "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY2_VALUE", "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY3_VALUE", "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY4_VALUE", "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                    this.StatisticsGrid.setRowTextStyle("STAT_KEY5_VALUE", "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                };
                BulkUpdate.prototype.calculateStatistics = function () {
                    var _this = this;
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                        var TotalPiece = 0;
                        this.AssignmentGrid.forEachRow(function (id) {
                            TotalPiece += parseInt(_this.AssignmentGrid.cells(id, 4).getValue());
                        });
                        this.StatisticsGrid.cells("STAT_KEY1_VALUE", 0).setValue(TotalPiece);
                    }
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER) {
                        var TotalPiece = 0;
                        this.AssignmentGrid.forEachRow(function (id) {
                            TotalPiece += parseInt(_this.AssignmentGrid.cells(id, 4).getValue());
                        });
                        this.StatisticsGrid.cells("STAT_KEY1_VALUE", 0).setValue(TotalPiece);
                    }
                };
                BulkUpdate.prototype.setSpecificBeforeSave = function () {
                    var _this = this;
                    var AllBillNos = [];
                    this.AssignmentGrid.forEachRow(function (id) {
                        var BillNo = _this.AssignmentGrid.cells(id, 1).getValue();
                        AllBillNos.push(BillNo);
                    });
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                        this.ParameterJSON["ALL_BILL_NO"] = AllBillNos;
                        this.ParameterJSON["MASTER_NAME=STR"] = this.QueryForm.getItemValue("MASTER_NAME=STR");
                        this.ParameterJSON["TAILOR_NAME=STR"] = this.QueryForm.getItemValue("TAILOR_NAME=STR");
                        this.ParameterJSON["ASSIGNMENT_DATE=DATE"] = this.QueryForm.getItemValue("ASSIGNMENT_DATE=DATE", true);
                        this.ParameterJSON["LOCATION=STR"] = this.QueryForm.getItemValue("LOCATION=STR");
                    }
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER) {
                        this.ParameterJSON["ALL_BILL_NO"] = AllBillNos;
                        this.ParameterJSON["FINISHER_NAME=STR"] = this.QueryForm.getItemValue("FINISHER_NAME=STR");
                        this.ParameterJSON["IRON=STR"] = this.QueryForm.getItemValue("IRON=STR");
                        this.ParameterJSON["FINISHING_DATE=DATE"] = this.QueryForm.getItemValue("FINISHING_DATE=DATE", true);
                        this.ParameterJSON["LOCATION=STR"] = this.QueryForm.getItemValue("LOCATION=STR");
                    }
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_SINGLE) {
                        this.ParameterJSON["ALL_BILL_NO"] = AllBillNos;
                        this.ParameterJSON["TYPE=STR"] = "TO_" + this.QueryForm.getItemValue("TYPE=STR");
                        this.ParameterJSON["NAME=STR"] = this.QueryForm.getItemValue("NAME=STR");
                        this.ParameterJSON["ASSIGNMENT_DATE=DATE"] = this.QueryForm.getItemValue("ASSIGNMENT_DATE=DATE", true);
                        this.ParameterJSON["LOCATION=STR"] = this.QueryForm.getItemValue("LOCATION=STR");
                    }
                };
                BulkUpdate.prototype.setSpecificOnLoad = function () {
                    var _this = this;
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                        this.QueryForm.setItemValue("ASSIGNMENT_DATE=DATE", getCurrentDate());
                    }
                    else if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER) {
                        this.QueryForm.setItemValue("FINISHING_DATE=DATE", getCurrentDate());
                    }
                    else if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_SINGLE) {
                        this.QueryForm.setItemValue("ASSIGNMENT_DATE=DATE", getCurrentDate());
                        this.QueryForm.attachEvent("onChange", function (name, value) {
                            if (name == "TYPE=STR") {
                                _this.ModifiedLayoutObject.progressOn();
                                com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.QueryForm.getOptions("NAME=STR"), "EMPLOYEES", "EMP_NAME", "EMP_ROLE", value);
                                progressOffCustom(_this.ModifiedLayoutObject);
                            }
                        });
                    }
                };
                BulkUpdate.prototype.setFormStateAfterSave = function (Response) {
                    var _this = this;
                    var HelpCell;
                    var IconCell;
                    if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                        HelpCell = 10;
                        IconCell = 11;
                    }
                    else if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER) {
                        HelpCell = 7;
                        IconCell = 8;
                    }
                    else if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_SINGLE) {
                        HelpCell = 10;
                        IconCell = 11;
                    }
                    this.AssigmentStatus = "END";
                    this.AssignmentGrid.forEachRow(function (id) {
                        var BILLNO = _this.AssignmentGrid.cells(id, 1).getValue();
                        _this.AssignmentGrid.setUserData(id, "SERVER_DATA", Response.RESPONSE_VALUE[BILLNO]);
                        if (Response.RESPONSE_VALUE[BILLNO].split(",")[0].indexOf("SUCCES") == 0) {
                            _this.AssignmentGrid.cells(id, HelpCell).setValue("");
                            _this.AssignmentGrid.setRowTextStyle(id, "color:#0026ff;background-color: #cccccc; font-weight:bold; ");
                            _this.AssignmentGrid.cells(id, IconCell).setValue("<img height='23px' width='20px' src='resources/Images/success.png'/>");
                        }
                        else {
                            _this.AssignmentGrid.cells(id, HelpCell).setValue("<input type='button' value='HELP'/>");
                            _this.AssignmentGrid.cells(id, IconCell).setValue("<img height='20px' width='20px' src='resources/Images/failed.png'/>");
                        }
                    });
                };
                return BulkUpdate;
            }());
            bulkupdate.BulkUpdate = BulkUpdate;
        })(bulkupdate = ordermanager.bulkupdate || (ordermanager.bulkupdate = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
