FROM reg.bitgadak.com/closememo/mailsender-base:0.1

EXPOSE 10083

RUN mkdir -p /home/deployer/deploy
COPY ./build/libs/mailsender.jar /home/deployer/deploy

ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE /home/deployer/deploy/mailsender.jar
