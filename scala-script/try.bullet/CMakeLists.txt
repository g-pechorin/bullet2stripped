cmake_minimum_required (VERSION 3.0)

if(WIN32)
	add_definitions(-D_CRT_SECURE_NO_WARNINGS)
else(WIN32)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -std=gnu++11 -static-libstdc++")
	set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -static-libstdc++")
endif(WIN32)

project(bullettry)

add_definitions(-DCMAKE_BINARY_DIR="${CMAKE_BINARY_DIR}") 
add_definitions(-DCMAKE_SOURCE_DIR="${CMAKE_SOURCE_DIR}") 

include_directories(../target)

add_executable(
	bullettry
		bullettry.cpp
	)