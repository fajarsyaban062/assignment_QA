package api_automation

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import groovy.json.JsonSlurper

public class refactor_qa_assignment {

	private static final String BASE_URL = "https://reqres.in"

	/**
	 * Helper untuk soft assert. Memeriksa kesamaan.
	 * @param actual Nilai aktual
	 * @param expected Nilai yang diharapkan
	 * @param message Pesan verifikasi
	 */
	private void softAssertEquals(def actual, def expected, String message) {
		if (actual == expected) {
			KeywordUtil.logInfo("   [PASSED] ${message}. (Expected: ${expected}, Actual: ${actual})")
		} else {
			KeywordUtil.markFailed("   [FAILED] ${message}. (Expected: ${expected}, Actual: ${actual})")
		}
	}

	/**
	 * Helper untuk soft assert. Memeriksa tidak null.
	 * @param actual Objek yang akan diperiksa
	 * @param message Pesan verifikasi
	 */
	private void softAssertNotNull(def actual, String message) {
		if (actual != null) {
			KeywordUtil.logInfo("   [PASSED] ${message}. (Actual: ${actual})")
		} else {
			KeywordUtil.markFailed("   [FAILED] ${message}. (Actual: null)")
		}
	}

	/**
	 * Helper untuk soft assert. Memeriksa kondisi true.
	 * @param condition Kondisi yang harus true
	 * @param message Pesan verifikasi
	 * @param actualInfo Info tambahan jika gagal
	 */
	private void softAssertTrue(boolean condition, String message, def actualInfo = "") {
		if (condition) {
			KeywordUtil.logInfo("   [PASSED] ${message}.")
		} else {
			KeywordUtil.markFailed("   [FAILED] ${message}. (Actual: ${actualInfo})")
		}
	}

	/**
	 * [TC-01, TC-02]
	 * Memverifikasi halaman list user yang valid.
	 * @param pageNumber Nomor halaman yang akan diuji.
	 */
	@Keyword
	def verifyUserListPageIsValid(int pageNumber) {
		println "Menguji halaman valid: ${pageNumber}"
		int per_page = 6
		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users?page=" + pageNumber)
				.build()

		ResponseObject response = WS.sendRequest(request)
		int statusCode = response.getStatusCode()
		def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

		println(jsonResponse)

		if(statusCode == 200) {
			KeywordUtil.logInfo("Success status 200")
			softAssertEquals(jsonResponse.page, pageNumber, "Verifikasi nomor halaman di response")
			softAssertEquals(jsonResponse.per_page,per_page , "Properti 'per_page' tidak boleh null")
			softAssertNotNull(jsonResponse.total, "Properti 'total' tidak boleh null")
			softAssertNotNull(jsonResponse.total_pages, "Properti 'total_pages' tidak boleh null")
			softAssertTrue(jsonResponse.data instanceof List, "Properti 'data' harus berupa array/list", jsonResponse.data.getClass())
			softAssertNotNull(jsonResponse.support, "property support tidak boleh null")
			softAssertNotNull(jsonResponse._meta, "property meta tidak boleh null")
		}else if(statusCode == 401) {
			softAssertNotNull(jsonResponse.error, "missing api key")
		}
	}

	/**
	 * [TC-03]
	 * Memverifikasi bahwa endpoint default ke halaman 1 jika tidak ada parameter.
	 */
	@Keyword
	def verifyUserListDefaultsToPageOne() {
		println "Menguji default parameter (tanpa parameter page)"
		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users")
				.build()

		ResponseObject response = WS.sendRequest(request)
		//		String response200 = WS.verifyResponseStatusCode(response, 200)
		int statusCode = response.getStatusCode()
		def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

		if(statusCode == 401) {
			softAssertTrue(jsonResponse.data.email, "Email has verified")
			softAssertEquals(jsonResponse.data[0].id, 1, "Default must show id is 1")
			softAssertEquals(jsonResponse.data.page, 1, "Default must show page is 1")
		}else if(statusCode == 401) {
			softAssertNotNull(jsonResponse.error, "missing api key")
		}
	}
	/**
	 * [TC-04]
	 * Memverifikasi halaman yang tidak ada (di luar jangkauan).
	 * ReqRes mengembalikan 200 OK dengan data kosong.
	 */
	@Keyword
	def verifyUserListPageNotFound(int pageNumber) {
		println "Menguji halaman di luar jangkauan: ${pageNumber}"

		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users?page=" + pageNumber)
				.build()

		ResponseObject response = WS.sendRequest(request)
		WS.verifyResponseStatusCode(response, 401)
		def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

		softAssertNotNull(jsonResponse.error, "missing api key")
	}

	/**
	 * [TC-05, TC-06]
	 * Memverifikasi parameter halaman yang tidak valid (string, 0, negatif).
	 * ReqRes mengabaikannya dan default ke halaman 1.
	 */
	@Keyword
	def verifyUserListHandlesInvalidPageParam(String invalidPage) {
		println "Menguji parameter halaman tidak valid: ${invalidPage}"

		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users?page=" + invalidPage)
				.build()

		ResponseObject response = WS.sendRequest(request)
		WS.verifyResponseStatusCode(response, 401)
		def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

		softAssertNotNull(jsonResponse.error, "missing api key")
	}

	/**
	 * [TC-07]
	 * Memverifikasi format dan exist data.
	 */
	@Keyword
	def verifyUserPropertyListPageIsValid(int pageNumber) {
		println "Menguji halaman valid: ${pageNumber}"
		int per_page = 6
		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users?page=" + pageNumber)
				.build()

		ResponseObject response = WS.sendRequest(request)
		int statusCode = response.getStatusCode()
		def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

		println(jsonResponse)

		if(statusCode == 200) {
			if (jsonResponse.data.size() > 0) {
				for(int i=0; i>=6; i++) {
					def Users = jsonResponse.data[i]
					softAssertNotNull(Users.id, "User ID tidak boleh null")
					softAssertNotNull(Users.email , "User email tidak boleh null")
					softAssertNotNull(Users.first_name, "User first_name tidak boleh null")
					softAssertNotNull(Users.last_name, "User last_name tidak boleh null")
					softAssertNotNull(Users.avatar, "User avatar tidak boleh null")
					softAssertNotNull(Users.email.contains("@"), "User email harus berisi '@'")
				}
			}
		}else if(statusCode == 401) {
			softAssertNotNull(jsonResponse.error, "missing api key")
		}
	}

	/**
	 * [TC-08, TC-09]
	 * Memverifikasi header dan waktu respons.
	 */
	@Keyword
	def verifyResponseHeadersAndPerformance(long maxResponseTimeMs) {
		println "Menguji header dan performance (batas: ${maxResponseTimeMs}ms)"

		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users?page=2")
				.build()

		ResponseObject response = WS.sendRequest(request)
		int statusCode = response.getStatusCode()
		def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

		if(statusCode == 200) {
			String contentType = response.getHeaderField("Content-Type")
			softAssertTrue(contentType.contains("application/json"), "Content-Type harus application/json", contentType)

			long responseTime = response.getElapsedTime()
			softAssertTrue(responseTime <= maxResponseTimeMs, "Waktu respons (${responseTime}ms) melebihi batas (${maxResponseTimeMs}ms)", "${responseTime}ms")
		}else if(statusCode == 401) {
			softAssertNotNull(jsonResponse.error, "missing api key")
		}
	}

	/**
	 * [TC-10]
	 * Memverifikasi metode HTTP yang tidak diizinkan.
	 * API yang baik harus mengembalikan 405. ReqRes mungkin mengembalikan 404.
	 */
	@Keyword
	def verifyDisallowedMethods() {
		println "Menguji metode yang tidak diizinkan (POST, PUT, DELETE)"

		def methodsToTest = [
			"POST",
			"PUT",
			"DELETE",
			"PATCH"
		]

		for (String method in methodsToTest) {
			def request = new RestRequestObjectBuilder()
					.withRestRequestMethod(method)
					.withRestUrl(BASE_URL + "/api/users?page=2")
					.build()

			ResponseObject response = WS.sendRequest(request)
			WS.verifyResponseStatusCode(response, 401)

			String statusCode = response.getStatusCode().toString()
			softAssertTrue(statusCode.substring(0, 1) != "2", "Verifikasi status BUKAN 200 untuk method ${method}", "Actual status: ${statusCode}")
		}
	}

	/**
	 * [GUL-11]
	 * Memverifikasi/mencari seorang user spesifik di dalam data list halaman.
	 * @param pageNumber Halaman yang akan di-GET
	 * @param attribute Kunci JSON yang akan dicari (e.g., "email", "id", "first_name")
	 * @param expectedValue Nilai yang diharapkan untuk atribut tersebut
	 */
	@Keyword
	def verifyUserExistsOnPage(int pageNumber, String attribute, def expectedValue) {
		println "Mencari user di halaman ${pageNumber} dengan ${attribute} = ${expectedValue}"

		def request = new RestRequestObjectBuilder()
				.withRestRequestMethod("GET")
				.withRestUrl(BASE_URL + "/api/users?page=" + pageNumber)
				.build()

		ResponseObject response = WS.sendRequest(request)
		int statusCode = response.getStatusCode()

		if (statusCode == 200) {
			def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())

			def foundUser = jsonResponse.data.find { it."${attribute}" == expectedValue }
			softAssertNotNull(foundUser, "Verifikasi user dengan ${attribute} = ${expectedValue} ditemukan di halaman ${pageNumber}")

			if (foundUser != null) {
				KeywordUtil.logInfo("   [INFO] Data user yang ditemukan: ${foundUser}")
			}
		} else {
			KeywordUtil.markFailed("Gagal mengambil data halaman ${pageNumber}. Status: ${statusCode}")
		}
	}
}
