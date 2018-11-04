declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
declare var dhtmlx: any;
declare var dhtmlXDataView: any;
declare var getCurrentDate: any;
declare var scheduler: any;

module com.ordermanager.OrderScheduler {
    export class OrderScheduler {
        public LayoutCell: any;
        public NotificationCell: any;
        public SchedulerObject: any;

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
            this.SchedulerObject.load("LoadScheduleOrderData", () => {
                progressOffCustom(this.LayoutCell);
            });
            this.SchedulerObject.attachEvent("onClick", (id, e) => {
                var eventData = id.split("-");

                if (eventData[1].includes("TOTAL")) {//tOTAL
                    this.schedulerOrderListViewer("TOTAL", eventData[0]);
                    return true;
                }
                else if (!eventData[1].includes("NOT")) {//Its Ready
                    this.schedulerOrderListViewer("READY", eventData[0]);
                    return true;
                }
                else {//Not Ready
                    this.schedulerOrderListViewer("NOT_READY", eventData[0]);
                    return true;
                }

            });
        }
        public schedulerOrderListViewer(OrderStatusType: any, date: any) {
            var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Order List : "+OrderStatusType+" : "+date, 1250, 620);
            var OrderListGrid = winObj.attachGrid();
            OrderListGrid.enableMultiline(true);
            OrderListGrid.enableAutoWidth(true);
            OrderListGrid.load("loadScheduleOrderList?ORDER_STATUS="+OrderStatusType+"&DELIVERY_DATE="+date);
            OrderListGrid.attachEvent("onXLE", () => {
                
            });
            OrderListGrid.setStyle(
                "", "font-weight:bold;","color:red;", ""
            );
        }
        public addDailyStatsEvent(start: any, end: any, text: any) {
            this.SchedulerObject.addEvent({
                start_date: start,
                end_date: end,
                text: text
            });
        }
    }
}
