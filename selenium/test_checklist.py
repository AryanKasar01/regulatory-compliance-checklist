"""Optional browser smoke test. Install requirements and run while app is open on port 8084."""
import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait

class ChecklistBrowserTest(unittest.TestCase):
    def setUp(self):
        options = webdriver.ChromeOptions()
        options.add_argument("--headless=new")
        options.add_argument("--no-sandbox")
        self.driver = webdriver.Chrome(options=options)  # Selenium Manager handles driver setup
        self.driver.get("http://localhost:8084")

    def tearDown(self):
        self.driver.quit()

    def test_homepage_and_add_record(self):
        d = self.driver
        self.assertIn("Regulatory Compliance Checklist", d.title)
        d.find_element(By.ID, "title").send_keys("Selenium demo control")
        d.find_element(By.ID, "owner").send_keys("Automation QA")
        d.find_element(By.CSS_SELECTOR, "#recordForm button[type='submit']").click()
        WebDriverWait(d, 10).until(
            lambda driver: "Selenium demo control" in driver.find_element(By.ID, "records").text
        )

if __name__ == "__main__":
    unittest.main(verbosity=2)
