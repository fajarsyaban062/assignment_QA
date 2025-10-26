package loginDashboard

import java.awt.Robot
import java.awt.event.KeyEvent

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI




public class function {
	
	@Keyword
	def robotclick() {
		WebUI.delay(5)
		try {
			WebUI.comment("Mencoba menekan tombol ESCAPE untuk menutup pop-up browser")
			Robot robot = new Robot()
			robot.keyPress(KeyEvent.VK_ESCAPE)
			robot.keyRelease(KeyEvent.VK_ESCAPE)
			WebUI.delay(1) // Beri waktu 1 detik untuk pop-up menutup
		} catch (Exception e) {
			WebUI.comment("Gagal menggunakan Robot class, mungkin tidak masalah. Melanjutkan tes...")
		}
		
	}
}
