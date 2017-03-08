declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
declare var dhtmlx: any;
declare var dhtmlXDataView: any;
declare var getCurrentDate: any;
declare var scheduler:any;

module com.ordermanager.OrderScheduler {
    export class OrderScheduler {
        public LayoutCell: any;
        public NotificationCell: any;
        public SchedulerObject :any;

        constructor(LayoutCell: any, NotificationCell: any) {
            this.LayoutCell = LayoutCell;
            this.NotificationCell = NotificationCell;
            this.constructScheduler();

        }
        public constructScheduler() {
          //scheduler.config.api_date ="%d/%m/%y";
          scheduler.config.load_date = "%d/%m/%y";
          scheduler.config.xml_date = "%d/%m/%y";
          scheduler.config.show_loading = true;
          scheduler.config.readonly = true;          
          this.SchedulerObject = this.LayoutCell.attachScheduler(null, "month");
          this.SchedulerObject.setLoadMode("month");
          this.LayoutCell.progressOn();
          this.SchedulerObject.load("LoadScheduleOrderData", ()=> {
          progressOffCustom(this.LayoutCell);
          });
        }
        public addDailyStatsEvent(start:any,end:any,text:any){
                  this.SchedulerObject.addEvent({
                  start_date: start,
                  end_date:  end,
                  text:  text
              });
        }
      }
  }
