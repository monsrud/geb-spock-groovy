import geb.Page
import geb.Module
import geb.navigator.Navigator
import org.openqa.selenium.By



class SelectTimePeriodModule {

    static content = {
        selectTimePeriodMenu { $("a", text: contains("Select Time Period")) }
        selectTimePeriodDialog { $('h5', text: contains('Set Time Period')) }
        selectTimePeriodSubmit { $('button', text: contains('Okay')) }
        selectTimePeriodCancel { $('button', text: contains('Cancel')) }
    }

}

class ManageDevicesModule extends Module {
    static content = {
        manageDevicesMenu { $("a", text: contains("Manage Devices")) }
        manageDevicesDialog(required: false) { $('h5', text: contains('Manage Devices')) }
        manageDevicesSubmit { $('button', text: contains('Okay')) }
        manageDevicesCancel { $('button', text: contains('Cancel')) }
        setDeviceDescriptiveNameInputFields { $('input', type: 'text', class: 'form-control', maxlength: '50') }
        setDeviceIPAddressInputFields { $('input', type: 'text', class: 'form-control', maxlength: '15') }
        setDeviceGatewayInputFields { $('input', type: 'text', class: 'form-control', maxlength: '150') }
        manageDevicesRemove { $('button', text: contains('Remove')) }
        manageDevicesAdd { $('button', text: contains('Add')) }

        allTextInputFields { $('input', type: 'text', class: 'form-control') }

        allSelectFields { $('select', class: 'form-control' )}

        kbDeviceNames {
            $(By.xpath("//div[@class='modal-body']")).$('td', text: contains('KB-C6'))
        }
    }

    boolean isMenuOpen() {
        if (manageDevicesDialog.isDisplayed()) {
            return true
        } else {
            return false
        }
    }

    void open() {

        waitFor { manageDevicesMenu.isDisplayed() }

        if (!isMenuOpen()) {
            manageDevicesMenu.click()
            waitFor { manageDevicesDialog }
        }
    }

    void close() {
        if (isMenuOpen()) {
            manageDevicesMenu.click()
        }
    }

    ArrayList<String> getDeviceNames() {

        open()
        ArrayList<String> deviceNamesList = new ArrayList<String>()

        for (Navigator nav : kbDeviceNames) {
            deviceNamesList.add(nav.text())
        }

        close()
        return deviceNamesList
    }


    int getDeviceIndex(String kbDeviceName) {
        open()

        int i = 0
        for (Navigator nav : kbDeviceNames) {
            if (nav.text().contains(kbDeviceName))  {
                return i
            }
            i++
        }

        close()
        return -1
    }

    void addDevice(String kbDeviceName,
                   String descriptiveName,
                   String ipAddress,
                   String gatewayList,
                   boolean isGateway) {

        open()
        waitFor { manageDevicesAdd }
        manageDevicesAdd.click()

        allTextInputFields[24].value(gatewayList)
        allTextInputFields[23].value(ipAddress)
        allTextInputFields[22].value(descriptiveName)
        allTextInputFields[21].value(kbDeviceName)
        allSelectFields.last().value(true)

        Thread.sleep(20000)
        manageDevicesSubmit.click()

    }

    void removeDevice(String kbDeviceName) {
        open()

        int i = 0

        for (Navigator nav : kbDeviceNames) {
            if (nav.text() == kbDeviceName) {
                manageDevicesRemove[i].click()
                manageDevicesSubmit.click()
                break
            }
            i++
        }

    }

    void setDeviceGatewayList(String kbDeviceName, String gatewayList) {
        open()

        int i = 0

        for (Navigator nav : kbDeviceNames) {
            if (nav.text() == kbDeviceName) {
                setDeviceGatewayInputFields[i].value(gatewayList)
                manageDevicesSubmit.click()
                break
            }
            i++
        }

    }

    void setDeviceIPAddress(String kbDeviceName, String ipAddress) {
        open()

        int i = 0

        for (Navigator nav : kbDeviceNames) {
            if (nav.text() == kbDeviceName) {
                setDeviceIPAddressInputFields[i].value(ipAddress)
                manageDevicesSubmit.click()
                break
            }
            i++
        }

    }

    void setDeviceDescriptiveName(String kbDeviceName, String descriptiveName) {
        open()

        int i = 0

        for (Navigator nav : kbDeviceNames) {
            if (nav.text() == kbDeviceName) {
                setDeviceDescriptiveNameInputFields[i].value(descriptiveName)
                manageDevicesSubmit.click()
                break
            }
            i++
        }
    }
}

class SelectAStationModule extends Module {
    static content = {
        selectAStationMenu { $("a", text: contains("Select a Station")) }
        selectAStationMenuItems { $(By.className("dropdown-item")) }
        selectedStation { $("#root").find("h2", text: contains('Station:')) }

    }

    boolean isMenuOpen() {
        if (selectAStationMenu.getAttribute('aria-expanded') == 'true') {
            return true
        } else {
            return false
        }
    }

    ArrayList<String> getMenuItemsList() {
        ArrayList<String> menuItemsList
        for (Navigator nav : selectAStationMenuItems) {
            menuItemsList.add(nav.text())
        }
        return menuItemsList
    }

    void open() {
        if (!isMenuOpen()) {
            selectAStationMenu.click()
        }
    }

    void close() {
        if (isMenuOpen()) {
            selectAStationMenu.click()
        }
    }

    int getMenuItemIndex (Navigator navigator, String itemName) {
        int i = 0
        for (Navigator nav : navigator) {
            if (nav.text() == itemName) {
                return i
            }
            i++
        }
        return -1
    }

    void selectStation(String station_name) {
        open()

        for (Navigator nav : selectAStationMenuItems) {
            if (nav.text() == station_name) {
                nav.click()
                break
            }
        }
        waitFor { selectedStation }
    }
}

class FilterByDeviceModule extends Module {
    static content = {
        filterByDeviceMenu { $("a", text: contains("Filter by Device")) }
        filterByDeviceMenuItems { $('label', class: 'py-1 px-4 device-checkbox') }
        filterByDeviceMenuCheckboxes { $('label', class: 'py-1 px-4 device-checkbox').find('input') }
        multiSelect { $('label', text: contains('Multi-select')) }
        selectAll(required: false) { $('label', text: contains('Select-all')) }
        multiSelectCheckbox { $('label', text: contains('Multi-select')).find('input') }
        selectAllCheckbox { $('label', text: contains('Select all')).find('input') }
    }

    ArrayList<String> getMenuItemsList() {
        open()
        // get the text of the items shown in the menu
        List <String> menuItemsList = []
        for (Navigator nav : filterByDeviceMenuItems) {
            String trimmed = nav.text().trim().replaceAll(" +", " ")
            menuItemsList.add(trimmed)
        }
        close()
        return menuItemsList
    }



    void close() {
        if (isMenuOpen()) {
            filterByDeviceMenu.click()
        }
    }

    void open() {
        if (!isMenuOpen()) {
            filterByDeviceMenu.click()
        }
    }

    boolean isMenuOpen() {
        if (filterByDeviceMenu.getAttribute('aria-expanded') == 'true') {
            return true
        } else {
            return false
        }
    }

    void selectAllDevices() {
        open()
        if (!multiSelectCheckbox.getAttribute('checked')) {
            multiSelectCheckbox.value(true)
            waitFor { selectAllCheckbox }
        }

        if (!selectAllCheckbox.getAttribute('checked')) {
            selectAllCheckbox.value(true)
        }

        for (Navigator nav : filterByDeviceMenuCheckboxes) {
            assert nav.value() == 'on'
        }

        close()
    }

    void selectDevices(ArrayList devices) {
        open()

        if (!multiSelectCheckbox.getAttribute('checked')) {
            multiSelectCheckbox.value(true)
            waitFor { selectAllCheckbox }
        }

        // clear all the checkboxes
        if (!selectAllCheckbox.getAttribute('checked')) {
            selectAllCheckbox.value(true)
            Thread.sleep(1000)
            selectAllCheckbox.value(false)
        }

        for (Navigator nav : filterByDeviceMenuItems) {
            if (nav.text() == 'Multi-select') {
                nav.click()
            }
            for (String device : devices) {
                if (device == nav.text()) {
                    nav.click()
                }
            }
        }
        if (filterByDeviceMenu.getAttribute('aria-expanded') == 'true') {
            filterByDeviceMenu.click()
        }
    }
}


class DisplayOptionsModule extends Module {
    static content = {
        displayOptionsMenu { $("a", text: contains("Display Options")) }
        displayOptionsMenuItems { $('label', class: 'text-nowrap') }
    }

    void open() {
        if (!isMenuOpen()) {
            displayOptionsMenu.click()
        }
    }

    ArrayList<String> getMenuItemsList() {
        // get the text of the items shown in the menu
        ArrayList<String> menuItemsList
        for (Navigator nav : displayOptionsMenuItems) {
            menuItemsList.add(nav.text())
        }
        return menuItemsList

    }

    boolean isMenuOpen() {
        if (displayOptionsMenu.getAttribute('aria-expanded') == 'true') {
            return true
        } else {
            return false
        }
    }

}


class FilterByInterfaceModule extends Module {
    static content = {
        filterByInterfaceMenu { $("a", text: contains("Filter by Interface")) }
        filterByInterfaceMenuItems { $('label', class: 'py-1 px-4 device-checkbox') }
    }

    void open() {
        if (!isMenuOpen()) {
            filterByInterfaceMenu.click()
        }
    }

    ArrayList<String> getMenuItemsList() {
        // get the text of the items shown in the menu
        ArrayList<String> menuItemsList
        for (Navigator nav : filterByInterfaceMenuItems) {
            menuItemsList.add(nav.text())
        }
        return menuItemsList

    }

    boolean isMenuOpen() {
        if (filterByInterfaceMenu.getAttribute('aria-expanded') == 'true') {
            return true
        } else {
            return false
        }
    }
}

class CloudReportingDashboard extends Page {

    static at = { title == "Reporting.Cloud.Webhost" }
    static content = {

        selectedStation { $("#root").find("h2", text: contains('Station:')) }

        // the blue menu bar items
        dashboardMenu { $("a", text: contains("Dashboard")) }
        plansMenu { $("a", text: contains("Plans")) }
        plansHeading  { $("h2", text: contains("Plans")) }

        // the gray menu bar items
        selectAStationMenu { $("a", text: contains("Select a Station")) }
        selectAStationMenuItems { $('button', class: 'dropdown-item') }

        filterByDeviceMenu { $("a", text: contains("Filter by Device")) }
        filterByDeviceMenuItems { $('label', class: 'device-checkbox') }
        filteredByBanner { $('p', text: contains('Filtered by:')) }
        displayOptionsMenu { $("a", text: contains("Display Options")) }
        filterByInterfaceMenu { $("a", text: contains("Filter by Interface")) }
        selectTimePeriodMenu { $("a", text: contains("Select Time Period")) }
        selectTimePeriodDialog { $('h5', text: contains('Set Time Period')) }
        selectTimePeriodSubmit { $('button', text: contains('Okay')) }
        selectTimePeriodCancel { $('button', text: contains('Cancel')) }
        seeRawDataMenu { $("a", text: contains("See Raw Data")) }
        rawDataPageHeading { $('h2', text: contains('Raw Data for')) }
        rawDataCards { $('div', class: 'card-header') }
        setDeviceNameMenu { $("a", text: contains("Set Device Names")) }
        setDeviceNameDialog { $('h5', text: contains('Set Device Name')) }
        setDeviceNameSubmit { $('button', text: contains('Okay')) }
        setDeviceNameCancel { $('button', text: contains('Cancel')) }
        setDeviceNameInputFields { $('input', type: 'text', class: 'form-control') }
        refreshMenu { $("a", text: contains("Refresh")) }

        selectAStationModule { module SelectAStationModule }
        setDeviceNamesModule { module ManageDevicesModule }
        selectAStationModule { module SelectAStationModule }
        filterByDeviceModule { module FilterByDeviceModule }
        filterByInterfaceModule { module FilterByInterfaceModule }
        displayOptionsModule  { module DisplayOptionsModule }

    }
}

class CloudReportingPlansPage extends Page {
    static content = {
        createNewPlanMenu { $("a", text: contains("Create New")) }
        selectAStationMenu { $("a", text: contains("Select a Station")) }
        selectAStationMenuItems { $('button', class: 'dropdown-item') }
        refreshMenu { $("a", text: contains("Refresh")) }

    }

}
