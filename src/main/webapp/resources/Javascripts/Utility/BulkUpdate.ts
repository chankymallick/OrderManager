declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
declare var dhtmlx: any;
declare var dhtmlXDataView: any;
declare var getCurrentDate: any;

module com.ordermanager.bulkupdate {
    export class BulkUpdate {
        public UpdateModuleName: any;
        public LayoutCell: any;
        public QueryFormHeight: any;
        public NotificationCell: any;
        public ModifiedLayoutObject: any;
        public OperationToolbar: any;
        public QueryForm: any;
        public AssignmentGrid: any;
        public StatisticsGrid: any;
        public ParameterJSON = {};
        public AssigmentStatus = "START";


        constructor(UpdateModuleName: any, LayoutCell: any, NotificationCell: any, QueryFormHeight: any, AssignmentGridColumns: any) {
            this.UpdateModuleName = UpdateModuleName;
            this.LayoutCell = LayoutCell;
            this.NotificationCell = NotificationCell;
            this.QueryFormHeight = QueryFormHeight;
            this.constructLayout();
            this.constructQueryForm();
            this.constructAssignmentGrid();
            this.shortCutRegister();
        }
        public shortCutRegister() {
            shortcut.add("F8", () => {
                this.sendBulkQueryForUpdate();
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document,
                    'propagate': true
                });
        }

        public constructLayout() {
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
            this.OperationToolbar.attachEvent("onClick", (id) => {
                if (id === "update") {
                }
                if (id === "clear") {
                }
                if (id === "save_default") {
                }

            });
        }
        public constructQueryForm() {
            if (this.QueryForm != null || this.QueryForm != undefined) {
                this.QueryForm.unload();
            }
            this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
            this.QueryForm.load(this.UpdateModuleName + "_QueryForm");
            this.QueryForm.attachEvent("onEnter", () => {
                var ParameterValue = this.QueryForm.getValues();
                this.loadAssignmentGridData(ParameterValue);
            });
            this.QueryForm.attachEvent("onXLE", () => {
                this.QueryForm.setFocusOnFirstActive();
                this.setSpecificOnLoad();
            });

        }
        public showAlertBox(Message){
          dhtmlx.message({
              type:"alert-error",
              text:Message
          });
        }
        public constructAssignmentGrid() {
            this.AssignmentGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
            this.AssignmentGrid.load("LoadAssignmentGrid?gridname=" + this.UpdateModuleName + "&ParamJson=");
            this.AssignmentGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                this.AssignmentGrid.attachEvent("onRowSelect", (id, index) => {
                    if (index === 10 && this.AssigmentStatus == "START") {
                        dhtmlx.confirm({
                            type: "confirm",
                            text: "Do you want to remove selected Order?",
                            callback: (result) => {
                                if (result) {
                                    this.AssignmentGrid.deleteRow(id);
                                    this.calculateStatistics();
                                }
                            }
                        });
                    }
                    else if(index === 10 && this.AssigmentStatus == "END"){
                    this.showAlertBox(this.AssignmentGrid.getUserData(id,"SERVER_DATA"));
                    }
                });
            }
            this.statisticCounterConstruction();
        }
        public loadAssignmentGridData(Parameters: any) {
            this.ModifiedLayoutObject.progressOn();
            var GridData;
            var Response = SynchronousGetAjaxRequest(this.UpdateModuleName + "_Query?ParamData=" + JSON.stringify(Parameters), "", null);
            if (Response.RESPONSE_STATUS === "SUCCESS") {
                var GridData = Response.RESPONSE_VALUE.DATA.split(",");
                var RowNum = parseInt(this.AssignmentGrid.getRowsNum()) + 1;
                GridData.splice(0, 0, RowNum);
                if (this.isBillNoExistInAssignmentGrid(GridData[1])) {
                    this.AssignmentGrid.addRow(RowNum, GridData);
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
                        this.calculateStatistics();
                        this.QueryForm.setItemValue("BILL_NO=STR", "");
                    }
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
        }
        public sendBulkQueryForUpdate() {
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

        }
        public isBillNoExistInAssignmentGrid(BillNo: any) {
            var status = true;
            if (this.AssignmentGrid.getRowsNum() == 0) {
                status = true;
            }
            this.AssignmentGrid.forEachRow((id) => {
                if (this.AssignmentGrid.cells(id, 1).getValue() === BillNo) {
                    showFailedNotificationWithICON("Bill no already added in List");
                    status = false;
                }
            });
            return status;
        }
        public statisticCounterConstruction() {
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

        }
        public calculateStatistics() {
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                var TotalPiece = 0;
                this.AssignmentGrid.forEachRow((id) => {
                    TotalPiece += parseInt(this.AssignmentGrid.cells(id, 4).getValue());
                });
                this.StatisticsGrid.cells("STAT_KEY1_VALUE", 0).setValue(TotalPiece);
            }
        }
        public setSpecificBeforeSave() {
            var AllBillNos = [];
            this.AssignmentGrid.forEachRow((id) => {
                var BillNo = this.AssignmentGrid.cells(id, 1).getValue();
                AllBillNos.push(BillNo);
            });
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                this.ParameterJSON["ALL_BILL_NO"] = AllBillNos;
                this.ParameterJSON["MASTER_NAME=STR"] = this.QueryForm.getItemValue("MASTER_NAME=STR");
                this.ParameterJSON["TAILOR_NAME=STR"] = this.QueryForm.getItemValue("TAILOR_NAME=STR");;
                this.ParameterJSON["ASSIGNMENT_DATE=DATE"] = this.QueryForm.getItemValue("ASSIGNMENT_DATE=DATE", true);
            }
        }
        public setSpecificOnLoad() {
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
                this.QueryForm.setItemValue("ASSIGNMENT_DATE=DATE", getCurrentDate());
            }
        }
        public setFormStateAfterSave(Response: any) {
            if (this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR) {
              this.AssigmentStatus="END";
              this.AssignmentGrid.forEachRow((id) => {
                  var BILLNO = this.AssignmentGrid.cells(id, 1).getValue()
                  this.AssignmentGrid.setUserData(id,"SERVER_DATA",Response.RESPONSE_VALUE[BILLNO]);
                  if(Response.RESPONSE_VALUE[BILLNO].split(",")[0].indexOf("SUCCES") == 0){
                  this.AssignmentGrid.cells(id, 10).setValue("");
                  this.AssignmentGrid.setRowTextStyle(id, "color:#b7b7b7;background-color: #cccccc; font-weight:bold; ");
                  this.AssignmentGrid.cells(id, 11).setValue("<img height='23px' width='20px' src='resources/Images/success.png'/>");
                  }
                  else{
                  this.AssignmentGrid.cells(id, 10).setValue("<input type='button' value='HELP'/>");
                  this.AssignmentGrid.cells(id, 11).setValue("<img height='20px' width='20px' src='resources/Images/failed.png'/>");
                  }
               });
            }
        }

    }
}
