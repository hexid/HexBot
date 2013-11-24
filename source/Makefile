all: prep csharp java

prep:
		mkdir -p bin

csharp:
		mcs -langversion:3 -out:bin/HexBot.exe -recurse:'src/csharp/**.cs'

java:
		ant -silent

clean:
		rm -rf bin

update: prep csharp java
		mv bin/HexBot.exe ../HexBot.exe
		mv bin/HexBot.jar ../HexBot.jar
		@$(MAKE) --no-print-directory clean
