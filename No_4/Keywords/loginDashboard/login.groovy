package loginDashboard
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


public class login {

	@Keyword
	def doLogin(String username, String password, String scenario) {

		WebUI.comment("direct to sauce demo")
		WebUI.openBrowser("https://www.saucedemo.com/")
		WebUI.maximizeWindow()
		WebUI.verifyElementPresent(findTestObject("Object Repository/label_swag_labs"), 5)
		WebUI.takeScreenshot()

		WebUI.comment("Input username")
		WebUI.setText(findTestObject("Object Repository/input_username"), username)
		WebUI.takeScreenshot()

		WebUI.comment("Input password")
		WebUI.setText(findTestObject("Object Repository/input_password"), password)
		WebUI.takeScreenshot()

		WebUI.comment("Click button login")
		WebUI.click(findTestObject("Object Repository/btn_login"))
		WebUI.takeScreenshot()

//		new loginDashboard.function().robotclick()

		def validUsers = [
			"standard_user",
			"problem_user",
			"performance_glitch_user",
			"visual_user"
		]
		def invalidUsers = [
			"locked_out_user",
			"error_user"
		]

		switch(scenario) {
			case "usernamepasswordRequired":
				WebUI.comment("Show username is required")
				WebUI.waitForElementPresent(findTestObject("Object Repository/label_username_is_required"), 5)
				break
			case "passwordisRequired":
				WebUI.comment("Password is required")
				WebUI.waitForElementVisible(findTestObject("Object Repository/label_password_is_required"), 5)
				break
			case "didnotmatch":
				WebUI.comment("username and password did not match")
				WebUI.waitForElementPresent(findTestObject("Object Repository/label_username_and_password_did_not_match"), 5)
				break
			default :
				if(invalidUsers.contains(username) && password == 'secret_sauce') {

					if(username == 'locked_out_user') {

						WebUI.comment("User has been locked")
						WebUI.verifyElementPresent(findTestObject("Object Repository/label_user_has_been_locked_out"), 5)
						WebUI.takeScreenshot()
					}else if(username == 'error_user') {

						WebUI.comment("User cannot clickable the element")
						WebUI.delay(3)
						WebUI.click(findTestObject("Object Repository/dashboard_filter_option"))
						WebUI.takeScreenshot()
					}
				}else if(validUsers.contains(username) && password == 'secret_sauce'){

					if(username == 'standard_user') {

						WebUI.comment("Click one of product")
						WebUI.verifyElementPresent(findTestObject("Object Repository/span_products"), 5)
						WebUI.click(findTestObject("Object Repository/dashboard_btn_sauce_labs_backpack"))
						WebUI.waitForElementPresent(findTestObject("Object Repository/dashboard_btn_back_to_product"), 5)
						WebUI.takeScreenshot()
					}else if(username == 'problem_user') {
						WebUI.comment("User cannot clickable the element")
						WebUI.verifyElementPresent(findTestObject("Object Repository/span_products"), 5)
						WebUI.click(findTestObject("Object Repository/dashboard_btn_add_to_card"))
						WebUI.click(findTestObject("Object Repository/dashboard_btn_remove_saucelabs_backpack"))
						WebUI.verifyElementNotPresent(findTestObject("Object Repository/dashboard_btn_add_to_card"), 5)
					}else if(username == 'performance_glitch_user') {
						WebUI.comment("glitch to wait the element present")
						WebUI.waitForElementPresent(findTestObject("Object Repository/span_products"), 20)
					}else if(username == 'visual_user') {
						WebUI.comment("Show my shopping")
						WebUI.verifyElementPresent(findTestObject("Object Repository/dashboard_label_shopping_cart"), 5)
					}
				}else {

					KeywordUtil.markFailed("dashboard not clickable")
				}

				break
		}

		WebUI.closeBrowser()
	}
}
