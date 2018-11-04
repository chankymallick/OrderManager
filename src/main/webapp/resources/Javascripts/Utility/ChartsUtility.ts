declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
declare var $: any;
declare var dhtmlXList: any;
declare var getCurrentDate: any;
declare var dhx: any;
declare var enlargeCharts: any;

module com.ordermanager.utility {
    export class ChartsUtility {

        public ChartLayout: any;
        public ChartSublayout: any;
        public AccordionObject: any;
        public TabbarObject: any
        public TaskListGrid: any;
        public ChatParams: any;

        constructor(public objectToAttach: any, public chartName: any) {
            this.TabbarObject = this.objectToAttach.attachTabbar({
                tabs: [
                    {
                        id: "a1",
                        text: "Order Dashboard &nbsp;",
                        active: true
                    },
                    {
                        id: "a2",
                        text: "Sales Dashboard &nbsp;",
                        active: false
                    },
                    {
                        id: "a3",
                        text: "Admin Dashboard &nbsp;",
                        active: false
                    }
                ]
            });
            this.ChartLayout = this.TabbarObject.cells("a1").attachLayout({
                pattern: "5I",
                cells: [
                    { id: "a", text: "a", header: false, height: 30 },
                    { id: "b", text: "Order Status <a href='#' onClick='enlargeCharts(2)'>Large</a>", header: true, height: 240 },
                    { id: "c", text: "Order Location <a href='#' onClick='enlargeCharts(1)'>Large</a>", header: true },
                    { id: "d", text: "", header: false },
                    { id: "e", text: "e", header: false }
                ]
            });
            this.ChartSublayout = this.ChartLayout.cells("e").attachLayout({
                pattern: "2U",
                cells: [
                    { id: "a", text: "a", header: false },
                    { id: "b", text: "b", header: false, width: 300 }
                ]
            }
            );

            this.ChatParams = { 'DAYS': '100' };
            this.getOrderStatusChart("SMALL");
            this.getOrderLocationChart("SMALL");
            //this.getOrderPieChart();
            this.getBarChart();
            this.taskList();
        }
        public getOrderStatusChart(size) {
            var data = [{ "color": "orange", "CURRENT_STATUS": "IN_PROCESS", "ID": "1", "STOCK": "23" }, { "color": "red", "CURRENT_STATUS": "NEW_ORDER", "ID": "2", "STOCK": "73" }, { "color": "green", "CURRENT_STATUS": "READY_TO_DELIVER", "ID": "3", "STOCK": "22" }];
            var Container;
            var CircleMargin;
            var width;
            var legendTextSize = "12px";
            if (size === "SMALL") {
                Container = this.ChartLayout.cells("b");
                CircleMargin = 230;
                width = 70;
            }
            else {
                Container = com.ordermanager.utilty.MainUtility.getModelWindow("Order Location", 800, 550);
                CircleMargin = 435;
                width = 100;
                legendTextSize = "16px";
            }
            var config = {
                view: "donut",
                value: "#STOCK#",
                x: CircleMargin,
                color: "#color#",
                tooltip: "<b>#CURRENT_STATUS#:#STOCK#</b>",
                legend: {
                    width: width,
                    align: "center",
                    valign: "middle",
                    template: "<b style='font-size:" + legendTextSize + ";'>#CURRENT_STATUS#</b>"
                },
                gradient: 1,
                shadow: true,
                pieInnerText: "<b>#STOCK#</b>",
                marker: {
                    type: "round",
                    width: 15
                }
            }
            var myPieChart = Container.attachChart(config);
            myPieChart.load("getChartData?chartName=orderStatus&chartParams=" + encodeURI(JSON.stringify(this.ChatParams)), "json");
            myPieChart.attachEvent("onItemclick", (id, ev, trg) => {
                this.OrderListViewer("ORDER_STATUS", myPieChart.get(id).CURRENT_STATUS);
            });

        }
        public getOrderLocationChart(size) {
            var Container;
            var CircleMargin;
            var width;
            var legendTextSize = "12px";
            if (size === "SMALL") {
                Container = this.ChartLayout.cells("c");
                CircleMargin = 230;
                width = 70;
            }
            else {
                Container = com.ordermanager.utilty.MainUtility.getModelWindow("Order Location", 800, 550);
                CircleMargin = 435;
                width = 100;
                legendTextSize = "16px";
            }

            var config = {
                view: "donut",
                value: "#STOCK#",

                x: CircleMargin,
                color: "#COLOR#",
                tooltip: "<b>#CURRENT_LOCATION#:#STOCK#</b>",
                legend: {
                    width: width,
                    align: "center",
                    valign: "middle",
                    template: "<b style='font-size:" + legendTextSize + ";'>#CURRENT_LOCATION#</b>"
                },
                gradient: 1,
                shadow: true,
                pieInnerText: "<b>#STOCK#</b>",
                marker: {
                    type: "round",
                    width: 15
                }
            }


            var myPieChart = Container.attachChart(config);
            myPieChart.load("getChartData?chartName=locationStatus&chartParams=" + encodeURI(JSON.stringify(this.ChatParams)), "json");
            myPieChart.attachEvent("onItemclick", (id, ev, trg) => {
                this.OrderListViewer("ORDER_LOCATION", myPieChart.get(id).CURRENT_LOCATION);
            });
        }
        public getOrderPieChart() {
            var config = {
                view: "donut",
                value: "#PIECE#",
                x: 230,
                color: "#color#",
                tooltip: "<b>#CURRENT_STATUS#:#PIECE#</b>",
                legend: {
                    width: 50,
                    align: "center",
                    valign: "middle",
                    template: "<b style='font-size:12px;'>#CURRENT_STATUS#</b>"
                },
                gradient: 0,
                shadow: true,
                pieInnerText: "<b>#PIECE#</b>",
                marker: {
                    type: "round",
                    width: 15
                }
            }
            var myPieChart = this.ChartLayout.cells("d").attachChart(config);
            myPieChart.load("getChartData?chartName=locationStatus&chartParams=" + encodeURI(JSON.stringify(this.ChatParams)), "json");
        }
        public getBarChart() {

            var config = {
                view: "stackedBar",
                container: "chart1",
                value: "#READY#",
                label: "<b>#READY#</b>",
                color: "#09ea54",
                gradient: "rising",
                width: 30,
                tooltip: {
                    template: "#READY#"
                },
                xAxis: {
                    template: "<i><b>#DATE#</b></i>"
                },
                yAxis: {},
                legend: {
                    values: [{ text: "READY", color: "#09ea54" }, { text: "NOT READY", color: "#e80000" }],
                    valign: "middle",
                    align: "right",
                    width: 120,
                    layout: "y"
                }
            }
            var barchart = this.ChartSublayout.cells("a").attachChart(config);
            barchart.addSeries({
                value: "#NOT_READY#",
                color: "#e80000",
                label: "<b>#NOT_READY#</b>",
                tooltip: {
                    template: "#NOT_READY#"
                }
            });
            barchart.load("getChartData?chartName=orderScheduler&chartParams=" + encodeURI(JSON.stringify(this.ChatParams)), "json");
            barchart.attachEvent("onItemclick", (id, ev, trg) => {                            
                this.OrderListViewer("FUTURE_ORDER", barchart.get(id).FULL_DATE);
            });
        }
        public OrderListViewer(ListType: any, param: any) {
            var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Order List : " + ListType + " : " + param, 1250, 620);
            var OrderListGrid = winObj.attachGrid();
            OrderListGrid.enableMultiline(true);
            OrderListGrid.enableAutoWidth(true);
            OrderListGrid.setStyle(
                "", "font-weight:bold;","color:red;", ""
            );
            OrderListGrid.load("loadChartOrderList?TYPE=" + ListType + "&PARAM_DATA=" + param);
            OrderListGrid.attachEvent("onXLE", () => {

            });

        }
        public taskList() {
            this.TaskListGrid = this.ChartSublayout.cells("b").attachGrid();
            this.TaskListGrid.enableMultiline(true);
            this.TaskListGrid.enableAutoWidth(true);
            this.TaskListGrid.load("LoadDataViewGrid?gridname=taskList");
            this.TaskListGrid.attachEvent("onXLE", () => {
                this.TaskListGrid.detachHeader(0);
            });
            this.TaskListGrid.attachEvent("onRowSelect", (id, ind) => {
            });
        }
        public updateTask(TaskId: number) {
            var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Update Task Status", 300, 305);
            var formConfig = [
                { type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130 },
                {
                    type: "block", width: "auto", blockOffset: 20, list: [
                        { type: "label", label: "SELECT STATUS", labelWidth: "200" },
                        {
                            type: "select", label: "TASK STATUS", name: "TASK_STATUS", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;", options: [
                                { text: "FINISHED", value: "FINISHED" },
                                { text: "PENDING", value: "PENDING" },
                                { text: "HOLD", value: "HOLD" },
                                { text: "CANCEL", value: "CANCEL" }
                            ]
                        },
                        { type: "calendar", label: "DATE", name: "RESOLVE_DATE", dateFormat: "%d/%m/%y", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey; " },
                        { type: "input", label: "NOTE", name: "NOTE", rows: "3", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey; " },
                        { type: "button", name: "SAVE", label: "New Input", value: "SAVE", className: "taskupdatebutton" }
                    ]
                }
            ];

            var formObj = winObj.attachForm(formConfig);
            formObj.setItemValue("RESOLVE_DATE", getCurrentDate());
            formObj.attachEvent("onButtonClick", (id) => {
                // if(id === "SAVE"){
                winObj.progressOn();
                var paramJson = {};
                paramJson["TASK_ID"] = TaskId;
                paramJson["TASK_STATUS"] = formObj.getItemValue("TASK_STATUS");
                paramJson["RESOLVE_DATE"] = formObj.getItemValue("RESOLVE_DATE", true);
                paramJson["NOTE"] = formObj.getItemValue("NOTE");
                var Response = SynchronousGetAjaxRequest("updateTask?ParamData=" + JSON.stringify(paramJson), "", null);
                if (Response.RESPONSE_STATUS === "SUCCESS") {
                    showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                    this.TaskListGrid.load("LoadDataViewGrid?gridname=taskList");
                    winObj.close();
                }
                if (Response.RESPONSE_STATUS === "FAILED") {
                    showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                }
                progressOffCustom(winObj);
                // }
            });
        }

        public static orderEnquiry(BillNo: any) {
            var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Order Enquiry : " + BillNo, 1250, 620);

            var searchLayout = winObj.attachLayout({
                pattern: "5H",
                cells: [
                    { id: "a", text: "a", header: false, width: 370 },
                    { id: "b", text: "Order Assignments", header: true },
                    { id: "c", text: "Payments", header: true },
                    { id: "d", text: "Items", header: true },
                    { id: "e", text: "Order Details", header: true, width: 420 }

                ]
            });
            var Response = SynchronousGetAjaxRequest("getSearchOrderMobilityDetails?BILL_NO=" + BillNo, "", null);
            if (Response.RESPONSE_STATUS === "SUCCESS") {
                searchLayout.cells("a").attachURL("resources/TemplateFiles/Diagram.html?DATA=" + JSON.stringify(Response.RESPONSE_VALUE.MOBILITY_DATA));
            }
            else {
                showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
            }
            var PaymentDataGrid = searchLayout.cells("c").attachGrid();
            PaymentDataGrid.enableMultiline(true);
            PaymentDataGrid.enableAutoWidth(true);
            PaymentDataGrid.load("LoadSearchGrid?gridname=paymentGrid&BillNo=" + BillNo);
            PaymentDataGrid.attachEvent("onXLE", () => {
                PaymentDataGrid.detachHeader(0);
            });
            var orderAssignmentGrid = searchLayout.cells("b").attachGrid();
            orderAssignmentGrid.enableMultiline(true);
            orderAssignmentGrid.enableAutoWidth(true);
            orderAssignmentGrid.load("LoadSearchGrid?gridname=assignmentGrid&BillNo=" + BillNo);
            orderAssignmentGrid.attachEvent("onXLE", () => {
                orderAssignmentGrid.detachHeader(0);
            });
            var orderItemsGrid = searchLayout.cells("d").attachGrid();
            orderItemsGrid.enableMultiline(true);
            orderItemsGrid.enableAutoWidth(true);
            orderItemsGrid.load("LoadSearchGrid?gridname=orderItemsGrid&BillNo=" + BillNo);
            orderItemsGrid.attachEvent("onXLE", () => {
                orderItemsGrid.detachHeader(0);
            });
            var Response = SynchronousGetAjaxRequest("getSearchOrderDetails?BILL_NO=" + BillNo, "", null);
            if (Response.RESPONSE_STATUS === "SUCCESS") {
                var ORDER_DATA = JSON.parse(Response.RESPONSE_VALUE.ORDER_DATA);
                var orderDetailGrid = searchLayout.cells("e").attachGrid();
                orderDetailGrid.enableMultiline(true);
                orderDetailGrid.enableAutoWidth(true);
                orderDetailGrid.setHeader("KEY,VALUE");
                orderDetailGrid.setInitWidths("200,220");
                orderDetailGrid.setColAlign("left,left");
                orderDetailGrid.setColTypes("ro,ro");
                orderDetailGrid.attachEvent("onXLE", () => {
                });
                orderDetailGrid.init();
                orderDetailGrid.detachHeader(0);
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> BILL NO </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.BILL_NO + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> ORDER DATE </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.ORDER_DATE + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> DELIVERY DATE</b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.DELIVERY_DATE + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> CUSTOMER NAME </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.CUSTOMER_NAME + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> ORDER TYPE </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.ORDER_TYPE + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> QUANTITY </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.QUANTITY + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> PRICE </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.PRICE + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> ADVANCE </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.ADVANCE + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> DISCOUNT </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.DISCOUNT + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> DUE </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.DUE + "</b>");
                if (ORDER_DATA.CURRENT_STATUS === "READY_TO_DELIVER" || ORDER_DATA.CURRENT_STATUS === "DELIVERY_COMPLETED") {
                    orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> CURRENT STATUS </b>,<b class='ORDER_SEARCH_VALUE_GREEN'>" + ORDER_DATA.CURRENT_STATUS + "</b>");
                    orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> CURRENT LOCATION </b>,<b class='ORDER_SEARCH_VALUE_GREEN'>" + ORDER_DATA.CURRENT_LOCATION + "</b>");
                }
                else {
                    orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> CURRENT STATUS </b>,<b class='ORDER_SEARCH_VALUE_RED'>" + ORDER_DATA.CURRENT_STATUS + "</b>");
                    orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> CURRENT LOCATION </b>,<b class='ORDER_SEARCH_VALUE_RED'>" + ORDER_DATA.CURRENT_LOCATION + "</b>");
                }

                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> SUB STATUS </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.SUB_STATUS + "</b>");

                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> NOTE </b>,<b class='ORDER_SEARCH_VALUE'>" + ORDER_DATA.NOTE + "</b>");
                orderDetailGrid.addRow("1", "<b class='ORDER_SEARCH_KEY'> IMAGES </b>,<b class='ORDER_SEARCH_VALUE'><a href='#' onClick='imageViewer(" + ORDER_DATA.BILL_NO + ")'>" + ORDER_DATA.IMAGE_COUNT + " - IMAGE</a></b>");


            }
            if (Response.RESPONSE_STATUS === "FAILED") {
                showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
            }


        }

    }

}

