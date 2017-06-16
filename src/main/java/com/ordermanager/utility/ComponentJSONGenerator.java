package com.ordermanager.utility;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
//addNewStatusType

@Controller
public class ComponentJSONGenerator {

    @Autowired
    UtilityDAO UtilityDAO;
@ModelAttribute
    public void MemoryMonitor(Model mvc) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory(); // current heap allocated to the VM process
        long freeMemory = runtime.freeMemory(); // out of the current heap, how much is free
        long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
        long usedMemory = totalMemory - freeMemory; // how much of the current heap the VM is using
        long availableMemory = maxMemory - usedMemory;
        
        System.out.println("Total : "+totalMemory);
        System.out.println("freeMemory : "+freeMemory);
        System.out.println("usedMemory : "+usedMemory);

             
    }
    @RequestMapping("/addNewOrder_Form")
    public ModelAndView addNewOrder(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewOrder"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewOrder");
            } else {
                requestMap.put("FormType", "addNewOrder_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewOrder");
        }
    }

    @RequestMapping("/quickNewOrder_Form")
    public ModelAndView addNewQuickOrder(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "quickNewOrder"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "quickNewOrder");
            } else {
                requestMap.put("FormType", "quickNewOrder_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "quickNewOrder");
        }
    }

    @RequestMapping("/addNewItem_Form")
    public ModelAndView loadNewOrderItemForm(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewItem"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewItem");
            } else {
                requestMap.put("FormType", "addNewItem_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewItem");
        }
    }

    @RequestMapping("/addNewStatusType_Form")
    public ModelAndView loadAddNewStatusType(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewStatusType"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewStatusType");
            } else {
                requestMap.put("FormType", "addNewStatusType_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewStatusType");
        }
    }

    @RequestMapping("/addNewLocation_Form")
    public ModelAndView loadaddNewLocation(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewLocation"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewLocation");
            } else {
                requestMap.put("FormType", "addNewLocation_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewLocation");
        }
    }

    @RequestMapping("/addNewUser_Form")
    public ModelAndView loadaddNewUser(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewUser"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewUser");
            } else {
                requestMap.put("FormType", "addNewUser_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewUser");
        }
    }

    @RequestMapping("/advanceReport_Form")
    public ModelAndView dailyAdvanceReport() {
        return new ModelAndView("LoadJSON", "FormType", "advanceReport_Form");

    }
    @RequestMapping("/daily_productionReport_Form")
    public ModelAndView dailyProductionReport() {
        return new ModelAndView("LoadJSON", "FormType", "daily_productionReport_Form");

    }
    @RequestMapping("/print_module_form")
    public ModelAndView PrintModuleForm() {
        return new ModelAndView("LoadJSON", "FormType", "print_module_form");

    }

    @RequestMapping("/updateNewOrder_QueryForm")
    public ModelAndView updateOrderQueryForm() {
        return new ModelAndView("LoadJSON", "FormType", "updateNewOrder_QueryForm");
    }

    @RequestMapping("/addadvance_QueryForm")
    public ModelAndView updateAdvanceQueryForm() {
        return new ModelAndView("LoadJSON", "FormType", "updateNewOrder_QueryForm");//Only Bill No
    }
    @RequestMapping("/updateBulkMasterTailor_QueryForm")
    public ModelAndView bulkAssignmentQueryForm() {
        return new ModelAndView("LoadJSON", "FormType", "updateBulkMasterTailor_QueryForm");//Only Bill No
    }
    @RequestMapping("/updateBulkReadyToDeliver_QueryForm")
    public ModelAndView bulkAssignmentReadyToDeliverQueryForm() {
        return new ModelAndView("LoadJSON", "FormType", "updateBulkReadyToDeliver_QueryForm");
    }

    @RequestMapping("/updateNewOrder_Form")
    public ModelAndView updateOrderForm(@RequestParam("ParamJson") JSONObject paramJson) {
        Map<String, Object> requestMap = new HashMap();
        Boolean isDefaultOn = true;
        JSONObject defaultData = UtilityDAO.getdefaultDataOrderFormWithExistingData(paramJson.getString("BILL_NO=STR"));
        requestMap.put("FormType", "updateNewOrder_Form");
        requestMap.put("FormData", defaultData);
        return new ModelAndView("LoadJSON", "ReqObject", requestMap);
    }

    @RequestMapping("/addadvance_Form")
    public ModelAndView updateAdvanceForm(@RequestParam("ParamJson") JSONObject paramJson) {
        Map<String, Object> requestMap = new HashMap();
        Boolean isDefaultOn = true;
        Map<String, Object> advanceData = UtilityDAO.getOrderPaymentData(paramJson.getString("BILL_NO=STR"));
        requestMap.put("FormType", "addadvance_Form");
        requestMap.put("FormData", advanceData);
        requestMap.put("BiLL_NO=STR", paramJson.getString("BILL_NO=STR"));
        return new ModelAndView("LoadJSON", "ReqObject", requestMap);
    }

    @RequestMapping("/addNewEmployee_Form")
    public ModelAndView addNewEmployee(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewEmployee"));
            if (defaultData.length() == 0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewEmployee");
            } else {
                requestMap.put("FormType", "addNewEmployee_withValue");
                requestMap.put("FormData", defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }
        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewEmployee");
        }
    }

    @RequestMapping("/operationMenu")
    public ModelAndView operationMenu(Model model) {
        return new ModelAndView("LoadJSON", "FormType", "operationMenu");
    }

    @RequestMapping("/operationToolbar")
    public String operationToolbar(Model map, @RequestParam("formname") String FormName) {
        map.addAttribute("FormType", "operationToolbar");
        map.addAttribute("FormName", FormName);
        return "LoadJSON";
    }

   @RequestMapping("/wagePaymentUnpaidToolbar")
    public String wagePaymentUnpaidToolbar(Model map, @RequestParam("formname") String FormName) {
        map.addAttribute("FormType", "wagePaymentUnpaidToolbar");
        map.addAttribute("FormName", FormName);
        return "LoadJSON";
    }
   @RequestMapping("/printModuleToolbar")
    public String printModule(Model map, @RequestParam("formname") String FormName) {
        map.addAttribute("FormType", "printModuleToolbar");
        map.addAttribute("FormName", FormName);
        return "LoadJSON";
    }

    @RequestMapping("/operationToolbarUpdate")
    public String operationToolbarUpdate(Model map, @RequestParam("formname") String FormName) {
        map.addAttribute("FormType", "operationToolbarUpdate");
        map.addAttribute("FormName", FormName);
        return "LoadJSON";
    }
    @RequestMapping("/operationToolbarBulkUpdate")
    public String operationToolbarBulkUpdate(Model map, @RequestParam("formname") String FormName) {
        map.addAttribute("FormType", "operationToolbarBulkUpdate");
        map.addAttribute("FormName", FormName);
        return "LoadJSON";
    }

}
