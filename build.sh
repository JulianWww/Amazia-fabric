rm -r build
./gradlew build

rm build/libs/*sources*
rm ~/.minecraft/mods/amazia*
rm ~/Downloads/server/mods/amazia*

cp ./build/libs/amazia*.jar ~/.minecraft/mods
mv ./build/libs/amazia*.jar ~/Downloads/server/mods
