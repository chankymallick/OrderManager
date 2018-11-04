var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var OrderScheduler;
        (function (OrderScheduler_1) {
            var OrderScheduler = /** @class */ (function () {
                function OrderScheduler(LayoutCell, NotificationCell) {
                    this.LayoutCell = LayoutCell;
                    this.NotificationCell = NotificationCell;
                    this.constructScheduler();
                }
                OrderScheduler.prototype.constructScheduler = function () {
                    var _this = this;
                    //scheduler.config.api_date ="%d/%m/%y";
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
                    this.SchedulerObject.attachEvent("onClick", function (id, e) {
                        var eventData = id.split("-");
                        if (eventData[1].includes("TOTAL")) {
                            _this.schedulerOrderListViewer("TOTAL", eventData[0]);
                            return true;
                        }
                        else if (!eventData[1].includes("NOT")) {
                            _this.schedulerOrderListViewer("READY", eventData[0]);
                            return true;
                        }
                        else {
                            _this.schedulerOrderListViewer("NOT_READY", eventData[0]);
                            return true;
                        }
                    });
                };
                OrderScheduler.prototype.schedulerOrderListViewer = function (OrderStatusType, date) {
                    var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Order List : " + OrderStatusType + " : " + date, 1250, 620);
                    var OrderListGrid = winObj.attachGrid();
                    OrderListGrid.enableMultiline(true);
                    OrderListGrid.enableAutoWidth(true);
                    OrderListGrid.load("loadScheduleOrderList?ORDER_STATUS=" + OrderStatusType + "&DELIVERY_DATE=" + date);
                    OrderListGrid.attachEvent("onXLE", function () {
                    });
                    OrderListGrid.setStyle("", "font-weight:bold;", "color:red;", "");
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
