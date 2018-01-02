javac Sprincli.java
jar cvfe Sprincli.jar Sprincli *.class
chmod +x Sprincli.jar

cd .. && rm -rf build
mkdir build
cd src
cp Sprincli.jar ../build &&
cp application.properties ../build &&
cp messages.properties ../build &&
cp pom.txt ../build &&
cp Startup.txt ../build &&

cp TemplateController.txt ../build &&
cp TemplateModel.txt ../build &&
cp TemplateRepository.txt ../build &&
cp TemplateService.txt ../build &&
cp TemplateValidator.txt ../build &&
cp TemplateView.txt ../build &&
cp TemplateClass.txt ../build &&
cp TemplateRouter.txt ../build &&

cp TemplateRouteController.txt ../build &&
cp TemplateUser.txt ../build &&
cp TemplateUserRepository.txt ../build &&
cp TemplateUserService.txt ../build &&
cp TemplateUserController.txt ../build &&

cp dashboard.txt ../build &&
cp newUser.txt ../build &&

rm Sprincli.jar