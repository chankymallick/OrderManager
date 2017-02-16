declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;

module com.ordermanager.bulkupdate {
    export class BulkUpdate {
        public UpdateModuleName: any;
        public LayoutCell: any;
        public QueryFormHeight: any;
        public NotificationCell: any;
        public ModifiedLayoutObject: any;
        public OperationToolbar: any;
        public QueryForm: any;
        public AssignmentGrid:any;


        constructor(UpdateModuleName: any, LayoutCell: any, NotificationCell: any, QueryFormHeight: any,AssignmentGridColumns:any){
            this.UpdateModuleName = UpdateModuleName;
            this.LayoutCell = LayoutCell;
            this.NotificationCell = NotificationCell;
            this.QueryFormHeight = QueryFormHeight;
            this.constructLayout();
            this.constructQueryForm();
            this.constructAssignmentGrid();
        }
        public constructLayout(){
            this.ModifiedLayoutObject = this.LayoutCell.attachLayout({
                pattern: "3T",
                cells: [
                    { id: "a", text: "Query", header: false, height: this.QueryFormHeight },
                    { id: "b", text: "Assigned Orders", header: true, width:800 },
                    { id: "c", text: "Statistics", header: true, }
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
            });

        }
        public constructAssignmentGrid(){
          this.AssignmentGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
          this.AssignmentGrid.load("LoadAssignmentGrid?gridname=" + this.UpdateModuleName + "&ParamJson=");
          this.AssignmentGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;","font-weight:bold;","","");


        }
        public loadAssignmentGridData(Parameters:any){
        var GridData;
        var Response = SynchronousGetAjaxRequest(this.UpdateModuleName + "_Query?ParamData=" + JSON.stringify(Parameters), "", null);
        if (Response.RESPONSE_STATUS === "SUCCESS") {
            var GridData = Response.RESPONSE_VALUE.DATA.split(",");
            var RowNum = parseInt(this.AssignmentGrid.getRowsNum())+1;
            GridData.splice(0, 0, RowNum);
            this.AssignmentGrid.addRow(RowNum,GridData);
            if(this.UpdateModuleName === com.ordermanager.home.OrderManagerHome.UPDATE_BULK_MASTER_TAILOR){
              if(GridData[5]>10){
                this.AssignmentGrid.setRowColor(RowNum,"#43fc2a");
              }
              if(GridData[5]<10){
                this.AssignmentGrid.setRowColor(RowNum,"#a7ff84");
              }
              if(GridData[5]<5){
                this.AssignmentGrid.setRowColor(RowNum,"#effc5f");
              }
              if(GridData[5]<3){
                this.AssignmentGrid.setRowColor(RowNum,"#ff7621");
              }
              if(GridData[5]<1){
               this.AssignmentGrid.setRowColor(RowNum,"#ff4800");
              }
              if(GridData[5]<0){
               this.AssignmentGrid.setRowColor(RowNum,"#ff0000");
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


        }
        public setSpecificFormSettingsoNLoad() {
        }

    }
}
