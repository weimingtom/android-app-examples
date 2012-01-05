#include "Global.h"

DECLARE_SINGLETON_MEMBER(Global);

Global::Global(void)
{

}

Global::~Global(void)
{
	gameLayer = NULL;
	gameMap = NULL;
	hero = NULL;
	controlLayer = NULL;
	gameScene = NULL;
}