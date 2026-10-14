# Regulatory Compliance Checklist — DevOps Mini Project

A small Java 17 + Spring Boot application demonstrating a Git → Maven → Jenkins → Docker workflow. It supports creating, viewing, updating, deleting and searching compliance records.

## Prerequisites (Windows)
- JDK 17 or 21
- Git
- Maven 3.9+
- Docker Desktop (running)
- Jenkins (already installed is fine)
- Optional: Python 3.10+ and Google Chrome for Selenium; Ansible is easiest to run from WSL/Linux

Check in PowerShell:
```powershell
java -version
git --version
mvn -version
docker --version
```

## 1. Run locally
Open PowerShell in this project folder:
```powershell
mvn clean test package
mvn spring-boot:run
```
Open http://localhost:8080. Stop with Ctrl+C.

## 2. Git and GitHub
Create an empty GitHub repository named `regulatory-compliance-checklist`. Then:
```powershell
git init
git add .
git commit -m "Initial Regulatory Compliance Checklist application"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/regulatory-compliance-checklist.git
git push -u origin main
git checkout -b feature/checklist-workflow
```
After making a small improvement:
```powershell
git add .
git commit -m "Add compliance record workflow"
git push -u origin feature/checklist-workflow
```
Replace YOUR-USERNAME with your GitHub username. Do not commit passwords, tokens or API keys.

## 3. Docker
Make sure Docker Desktop is running:
```powershell
docker build -t regulatory-checklist:1.0 .
docker rm -f regulatory-checklist-demo
docker run -d --name regulatory-checklist-demo -p 8084:8080 regulatory-checklist:1.0
docker ps
```
If the `docker rm` command says the container does not exist, that is okay. Open http://localhost:8084.
Logs and stop:
```powershell
docker logs regulatory-checklist-demo
docker stop regulatory-checklist-demo
```
If port 8084 is already occupied, use `-p 8085:8080` and update 8084 to 8085 in the Jenkinsfile and Selenium test.

## 4. Jenkins pipeline
This project includes `Jenkinsfile`. In Jenkins create **New Item → Pipeline**, name it `regulatory-compliance-checklist`, then select **Pipeline script from SCM**, SCM **Git**, and enter your GitHub repository URL and branch `*/main`. Save and click **Build Now**.

Jenkins must be able to run `mvn` and `docker` as the same Windows account/service. If `mvn` is not recognized, configure Maven under **Manage Jenkins → Tools** or add Maven's `bin` directory to the PATH of the Jenkins service account, then restart Jenkins. If Docker permission/connection fails, start Docker Desktop and ensure the Jenkins account can access it. Review **Console Output** for the exact failed stage.

If Jenkins runs on Linux, replace `bat` steps with equivalent `sh` steps.

## 5. Selenium browser test (optional)
Start the Docker app on port 8084 first. In PowerShell:
```powershell
cd selenium
py -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
python -m unittest -v test_checklist.py
```
If PowerShell blocks activation, use `.\.venv\Scripts\python.exe -m pip install -r requirements.txt` and `.\.venv\Scripts\python.exe -m unittest -v test_checklist.py`. Selenium Manager may need internet access to obtain a compatible Chrome driver.

## 6. Ansible verification (optional)
Ansible is normally run on Linux/WSL, not native Windows. In WSL with Ansible installed, from this project folder run:
```bash
ansible-playbook -i localhost, -c local ansible/deploy-checklist.yml
```
It checks Docker and verifies the application returns HTTP 200 on port 8084. If Ansible setup is not possible tonight, show the playbook and explain it is the configuration/verification stage, but do not claim it ran.

## 7. Demo checklist
1. Open the application and add a record.
2. Edit the record status to `Complete`.
3. Search by owner or task name.
4. Show GitHub commits and feature branch.
5. Show Maven test output.
6. Show Jenkins green build and stage logs.
7. Run `docker ps`, open http://localhost:8084, and show the container.
8. If available, show Selenium test output and Ansible verification output.

## Scope / limitations
This is a teaching/demo application. Records are held in memory and reset when the server restarts; it is not production-ready and does not implement real authentication or role-based authorization. Explain these as future improvements rather than claiming they are already implemented.
