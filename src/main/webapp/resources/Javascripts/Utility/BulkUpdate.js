var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var bulkupdate;
        (function (bulkupdate) {
            var BulkUpdate = (function () {
                function BulkUpdate(UpdateModuleName, LayoutCell, NotificationCell, QueryFormHeight, AssignmentGridColumns) {
                    this.UpdateModuleName = UpdateModuleName;
                    this.LayoutCell = LayoutCell;
                    this.NotificationCell = NotificationCell;
                    this.QueryFormHeight = QueryFormHeight;
                    this.constructLayout();
                    this.constructQueryForm();
                    this.constructAssignmentGrid();
                }
                BulkUpdate.prototype.constructLayout = function () {
                    this.ModifiedLayoutObject = this.LayoutCell.attachLayout({
                        pattern: "3T",
                        cells: [
                            { id: "a", text: "Query", header: false, height: this.QueryFormHeight },
                            { id: "b", text: "Assigned Orders", header: true, width: 800 },
                            { id: "c", text: "Statistics", header: true, }
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
                BulkUpdate.prototype.constructQueryForm = function () {
                    var _this = this;
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
                    });
                };
                BulkUpdate.prototype.constructAssignmentGrid = function () {
                    this.AssignmentGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
                    this.AssignmentGrid.load("LoadAssignmentGrid?gridname=" + this.UpdateModuleName + "&ParamJson=");
                    this.AssignmentGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
                };
                BulkUpdate.prototype.loadAssignmentGridData = function (Parameters) {
                    var GridData;
                    var Response = SynchronousGetAjaxRequest(this.UpdateModuleName + "_Query?ParamData=" + JSON.stringify(Parameters), "", null);
                    if (Response.RESPONSE_STATUS === "SUCCESS") {
                        var GridData = Response.RESPONSE_VALUE.DATA.split(",");
                        var RowNum = parseInt(this.AssignmentGrid.getRowsNum()) + 1;
                        GridData.splice(0, 0, RowNum);
                        this.AssignmentGrid.addRow(RowNum, GridData);
                        if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                            if (GridData[5] > 10) {
                                this.AssignmentGrid.setRowColor(RowNum, "#43fc2a");
                            }
                            if (GridData[5] < 10) {
                                this.AssignmentGrid.setRowColor(RowNum, "#a7ff84");
                            }
                            if (GridData[5] < 5) {
                                this.AssignmentGrid.setRowColor(RowNum, "#effc5f");
                            }
                            if (GridData[5] < 3) {
                                this.AssignmentGrid.setRowColor(RowNum, "#ff7621");
                            }
                            if (GridData[5] < 1) {
                                this.AssignmentGrid.setRowColor(RowNum, "#ff4800");
                            }
                            if (GridData[5] < 0) {
                                this.AssignmentGrid.setRowColor(RowNum, "#ff0000");
                            }
                        }
                        showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.collapse();
                    }
                    if (Response.RESPONSE_STATUS === "FAILED") {
                        showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.attachHTMLString("  : <b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                        this.NotificationCell.expand();
                    }
                };
                BulkUpdate.prototype.setSpecificFormSettingsoNLoad = function () {
                };
                return BulkUpdate;
            }());
            bulkupdate.BulkUpdate = BulkUpdate;
        })(bulkupdate = ordermanager.bulkupdate || (ordermanager.bulkupdate = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
