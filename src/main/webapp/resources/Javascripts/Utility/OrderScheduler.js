var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var OrderScheduler;
        (function (OrderScheduler_1) {
            var OrderScheduler = (function () {
                function OrderScheduler(LayoutCell, NotificationCell) {
                    this.LayoutCell = LayoutCell;
                    this.NotificationCell = NotificationCell;
                    this.constructScheduler();
                }
                OrderScheduler.prototype.constructScheduler = function () {
                    var _this = this;
                    scheduler.config.load_date = "%d/%m/%y";
                    scheduler.config.xml_date = "%d/%m/%y";
                    scheduler.config.show_loading = true;
                    scheduler.config.readonly = true;
                    this.SchedulerObject = this.LayoutCell.attachScheduler(null, "month");
                    this.SchedulerObject.setLoadMode("month");
                    this.LayoutCell.progressOn();
                    this.SchedulerObject.load("LoadScheduleOrderData", function () {
                        progressOffCustom(_this.LayoutCell);
                    });
                };
                OrderScheduler.prototype.addDailyStatsEvent = function (start, end, text) {
                    this.SchedulerObject.addEvent({
                        start_date: start,
                        end_date: end,
                        text: text
                    });
                };
                return OrderScheduler;
            }());
            OrderScheduler_1.OrderScheduler = OrderScheduler;
        })(OrderScheduler = ordermanager.OrderScheduler || (ordermanager.OrderScheduler = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
