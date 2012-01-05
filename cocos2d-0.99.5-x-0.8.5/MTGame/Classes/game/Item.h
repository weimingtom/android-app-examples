#pragma once

#include "cocos2d.h"

class Item : public cocos2d::CCObject
{
public:
	Item(void);
	~Item(void);

	int gid;
};
