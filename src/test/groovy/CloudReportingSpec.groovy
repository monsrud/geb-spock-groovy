
import geb.spock.GebSpec
import org.apache.commons.lang3.RandomStringUtils



class CloudReportingSpec extends GebSpec {

    String stationName = 'hq'
    String kbDevice = '00-00-05-84'
    Map<String,String> radios = [
        '00-00-01-68': '10.80.0.85',
        '00-00-05-5C': '10.80.0.77',
        '00-00-05-68': '10.80.0.75',
        '00-00-05-84': '10.80.0.86',
        '00-00-05-F4': '10.80.0.78',
        '00-00-06-2C': '10.80.0.81'
    ]

    def "Open Cloud Reporting Dashboard for Specific Station" () {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)

        when:
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        then:
        assert waitFor { cloudReportingDashboard.selectedStation.text().contains(stationName) }
    }

    def "Set Device Name Dialog Displayed"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        when:
        cloudReportingDashboard.setDeviceNamesModule.open()

        then:
        cloudReportingDashboard.setDeviceNameCancel.click()

    }

    def "Add Device"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        when:
        cloudReportingDashboard.setDeviceNamesModule.addDevice("foo", "foo", "foo", "foo", true)
        Thread.sleep(10000)

        then:
        true

    }

    def "Set Device Descriptive Name"() {
        // set a device's descriptive name
        // verify that the name is displayed in the devices menu
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)
        String generatedString = RandomStringUtils.random(8, true, true);

        when:
        cloudReportingDashboard.setDeviceNamesModule.setDeviceDescriptiveName(kbDevice,
                generatedString)
        cloudReportingDashboard.setDeviceNamesModule.getDeviceIndex(kbDevice)
        //System.out.println("THE INDEX IS " + str(index))

        then:
        assert cloudReportingDashboard.filterByDeviceModule.getMenuItemsList().contains(
                generatedString + " (${kbDevice})")

    }

    def "Plans page Displayed" () {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationMenu.click()
        cloudReportingDashboard.selectAStationMenuItems[0].click()

        when:
        cloudReportingDashboard.plansMenu.click()
        waitFor { cloudReportingDashboard.plansHeading }

        then:
        assert cloudReportingDashboard.plansHeading.isDisplayed()

    }

    def "Set Time Period Dialog Displayed"() {

        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        when:
        cloudReportingDashboard.selectTimePeriodMenu.click()
        waitFor { cloudReportingDashboard.selectTimePeriodDialog }

        then:
        cloudReportingDashboard.selectTimePeriodCancel.click()

    }

    def "Dashboard Refresh Page Button"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        when:
        String before = cloudReportingDashboard.filteredByBanner.text()
        cloudReportingDashboard.refreshMenu.click()
        Thread.sleep(5000)
        String after = cloudReportingDashboard.filteredByBanner.text()

        then:
        assert before != after
    }

    def "Navigate to Raw Data Page"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        when:
        cloudReportingDashboard.seeRawDataMenu.click()

        then:
        assert cloudReportingDashboard.rawDataPageHeading.text().contains('Raw Data for')
        assert cloudReportingDashboard.rawDataCards.size() == 6
    }

    def "Filter by Device: Select a Single Device"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)
        //Thread.sleep(10000)
        ArrayList<String> menuItems = cloudReportingDashboard.filterByDeviceModule.getMenuItemsList()
        ArrayList<String> singleDevice = new ArrayList<String>()
        singleDevice.add(menuItems[3])
        when:
        cloudReportingDashboard.filterByDeviceModule.selectDevices(singleDevice)
        waitFor { cloudReportingDashboard.filteredByBanner }

        then:
        String match = "Filtered by: " + menuItems[3] + ", Time Period:"
        String string = cloudReportingDashboard.filteredByBanner.text().trim()
        assert string.contains(match)

    }

    def "Filter by Device: Select All Devices Individually"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)

        ArrayList<String> menuItems = cloudReportingDashboard.filterByDeviceModule.getMenuItemsList()
        menuItems.removeAll { it.contains('Multi-select') }
        menuItems.removeAll { it.contains('Select all') }

        when:
        cloudReportingDashboard.filterByDeviceModule.selectDevices(menuItems)
        waitFor { cloudReportingDashboard.filteredByBanner }

        then:
        for (String menuItem : menuItems) {
            assert cloudReportingDashboard.filteredByBanner.text().contains(menuItem)
        }
    }

    def "Filter by Device: Select All Devices"() {
        CloudReportingDashboard cloudReportingDashboard
        given:
        cloudReportingDashboard = to(CloudReportingDashboard)
        cloudReportingDashboard.selectAStationModule.selectStation(stationName)
        ArrayList<String> menuItems = cloudReportingDashboard.filterByDeviceModule.getMenuItemsList()

        when:
        cloudReportingDashboard.filterByDeviceModule.selectAllDevices()

        then:
        for (String menuItem : menuItems) {
            assert cloudReportingDashboard.filteredByBanner.text().contains(menuItem)
        }


    }

}


/*
Metrics

When the metrics checkbox is selected under display options
the following 7 tiles should be shown.  Each tile should show
a value for each connected radio.

h6 containing :
Radio Frequency
RX Throughput   ... float Mbps
TX THroughput   ... float Mbps
Uptime          ... float days
Average Link Quality  ... float
Average RSSI          ... float
Average Processor Temporature  ... float



Display Options

Node Identity

When this item is selected, there should only be one tile shown

h6 Node Identity

ditto for LAN Status

Radio Peers and LAN Peers have 2 tiles


Quality & Temperature 2 tiles

RSSI .. One tile

RX/TX .. One tile

Throughput .. one tile


 */
