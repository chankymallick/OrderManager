declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
declare var $: any;
declare var dhtmlXList: any;

module com.ordermanager.utility {
    export class ChartsUtility {

        public ChartLayout: any;
        public ChartSublayout: any;
        public AccordionObject: any;
        public TabbarObject: any
        public TaskListGrid: any;
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
                    { id: "b", text: "b", header: false, height: 200 },
                    { id: "c", text: "c", header: false },
                    { id: "d", text: "d", header: false },
                    { id: "e", text: "e", header: false }
                ]
            });
            this.ChartSublayout = this.ChartLayout.cells("e").attachLayout({
                pattern: "2U",
                cells: [
                    { id: "a", text: "a", header: false },
                    { id: "b", text: "b", header: false, width:300 }
                ]
            }
            );


            this.getOrderStatusDonutChart();
            this.getOrderLocationPieChart();
            this.getOrderPieChart();
            this.getBarChart();
            this.taskList();
        }
        public getOrderStatusDonutChart() {
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
                gradient: 1,
                shadow: true,
                pieInnerText: "<b>#PIECE#</b>",
                marker: {
                    type: "round",
                    width: 15
                }
            }
            var myPieChart = this.ChartLayout.cells("b").attachChart(config);
            myPieChart.load("getChartData?chartName=orderStatus&chartParams={'DAYS':'100'}", "json");
        }
        public getOrderLocationPieChart() {
            var config = {
                view: "pie",
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
                gradient: 1,
                shadow: true,
                pieInnerText: "<b>#PIECE#</b>",
                marker: {
                    type: "round",
                    width: 15
                }
            }
            var myPieChart = this.ChartLayout.cells("c").attachChart(config);
            myPieChart.load("getChartData?chartName=locationStatus&chartParams={'DAYS':'10'}", "json");
        }
        public getOrderPieChart() {
            var config = {
                view: "pie",
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
            myPieChart.load("getChartData?chartName=locationStatus&chartParams={'DAYS':'50'}", "json");
        }
        public getBarChart() {
            var multiple_dataset = [
                { sales: "20", sales2: "35", sales3: "55", year: "02" },
                { sales: "40", sales2: "24", sales3: "40", year: "03" },
                { sales: "44", sales2: "20", sales3: "27", year: "04" },
                { sales: "23", sales2: "50", sales3: "43", year: "05" },
                { sales: "21", sales2: "36", sales3: "31", year: "06" },
                { sales: "50", sales2: "40", sales3: "56", year: "07" },
                { sales: "30", sales2: "65", sales3: "75", year: "08" },
                { sales: "90", sales2: "262", sales3: "155", year: "09" },
                { sales: "55", sales2: "40", sales3: "60", year: "10" },
                { sales: "72", sales2: "45", sales3: "54", year: "11" }
            ];


            var config = {
                view: "stackedBar",
                container: "chart1",
                value: "#sales#",
                label: "<b>#sales#</b>",
                color: "#7ed500",
                gradient: "rising",
                width: 30,
                tooltip: {
                    template: "#sales#"
                },
                xAxis: {
                    template: "<i><b>#year#</b></i>"
                },
                yAxis: {},
                legend: {
                    values: [{ text: "Type A", color: "#36abee" }, { text: "Type B", color: "#a7ee70" }, { text: "Type C", color: "#58dccd" }],
                    valign: "middle",
                    align: "right",
                    width: 90,
                    layout: "y"
                }
            }
            var barchart = this.ChartSublayout.cells("a").attachChart(config);
            barchart.addSeries({
                value: "#sales2#",
                color: "#3fc4f4",
                label: "<b>#sales2#</b>",
                tooltip: {
                    template: "#sales2#"
                }
            });
            barchart.parse(multiple_dataset, "json");

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


    }

}


   // [
            //     {"color":"white","CURRENT_STATUS":"DELIVERY_COMPLETED","PIECE":"1"},
            //     {"color":"orange","CURRENT_STATUS":"IN_PROCESS","PIECE":"2"},
            //     {"color":"red","CURRENT_STATUS":"NEW_ORDER","PIECE":"19"}
            // ]
            // var month_dataset = [
            //     { sales: "20", month: "Jan", color: "#ee3639" },
            //     { sales: "30", month: "Feb", color: "#ee9e36" },
            //     { sales: "50", month: "Mar", color: "#eeea36" },
            //     { sales: "40", month: "Apr", color: "#a9ee36" },
            //     { sales: "70", month: "May", color: "#36d3ee" },
            //     { sales: "80", month: "Jun", color: "#367fee" },
            //     { sales: "60", month: "Jul", color: "#9b36ee" }
            // ];
