/// <reference path="../Home/OrderManagerHome.ts"/>
/// <reference path="./Utility.ts"/>
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
module com.ordermanager.reportingutility {
    export class TransactionReports {
        public ModifiedLayoutObject: any;
        public MainLayout: any;
        public ReportName: any;
        public NotificationCell: any;
        public StatisticsCell:any;
        public StatisticsDataViewObject:any;
        public ParameterForm:any;
        constructor(layoutCell: any, notificationCell: any, reportName: any) {
            this.MainLayout = layoutCell;
            this.ReportName = reportName;
            this.NotificationCell = notificationCell;
            this.constructInnerLayoutForReport();
        }
        public constructInnerLayoutForReport() {
            this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                pattern: "3U",
                cells: [
                    { id: "a", text:"Statistics" ,height:170,width:775, header:false},
                    { id: "b", text:"Inputs",height:170, header:false},
                    { id: "c", text:"Details", header:false}
                ]
            });
            //this.contructStatisticsLayout();
            this.constructParameterForm();

        }
        public setSpecificOnLoad(){
          this.ParameterForm.attachEvent("onXLE", () => {
              this.ParameterForm.attachEvent("onButtonClick",(name)=>{
                this.StatisticsCell.progressOn();
                this.StatisticsCell.attachURL("getStatistics?StatisticsName="+this.ReportName+"&ReportParams="+this.getReportParameters());
                this.StatisticsCell.progressOff();
              });
              this.ParameterForm.setItemValue("ORDER_DATE=DATE",getCurrentDate());
              this.contructStatisticsLayout();
          });

        }
        public contructStatisticsLayout(){
          this.StatisticsCell = this.ModifiedLayoutObject.cells("a");
          this.StatisticsCell.fixSize(true, true);
          this.StatisticsCell.progressOn();
          this.StatisticsCell.attachURL("getStatistics?StatisticsName="+this.ReportName+"&ReportParams="+this.getReportParameters());
          this.StatisticsCell.progressOff();
        }
        public getReportParameters(){
            if(this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE){
             var params = this.ParameterForm.getValues();
             var date = this.ParameterForm.getItemValue("ORDER_DATE=DATE", true);
             params["ORDER_DATE=DATE"]=date;
             params["LOCATION=STR"]="BAGNAN";
             return JSON.stringify(params);
            }

        }
        public constructParameterForm(){
          if (this.ParameterForm != null || this.ParameterForm != undefined) {
              this.ParameterForm.unload();
          }
          this.ParameterForm = this.ModifiedLayoutObject.cells("b").attachForm();
          this.ParameterForm.load(this.ReportName+ "_Form");
          this.setSpecificOnLoad();
        }
    }
}
