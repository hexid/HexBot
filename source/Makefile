all: csharp java

csharp:
		mcs -langversion:3 -out:bin/HexBot.exe src/csharp/HexBot.cs

java:
		ant

clean:
		rm -rf bin

update: csharp java
		mv bin/HexBot.exe ../HexBot.exe
		mv bin/HexBot.jar ../HexBot.jar
		@$(MAKE) --no-print-directory clean