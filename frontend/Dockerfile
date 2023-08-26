# python:3.8.2-alpine: base docker image
# FROM python:3.8.2-alpine
# WORKDIR /frontend
# # copy all file from frontend to current work directory
# COPY . /frontend
# # port 80 (originally on web) can be accessed from local
# EXPOSE 80
# CMD [ "python3", "-m", "http.server", "80"]

FROM python:3.8.2-alpine
WORKDIR /frontend
COPY . /frontend
RUN pip install -r requirements.txt
ENV SERVER_PORT 80
EXPOSE 80
CMD [ "python3", "app.py"]