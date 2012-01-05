#ifndef __TELEPORT_H__
#define __TELEPORT_H__

#include "MTGame.h"

using namespace cocos2d;

class Teleport : public CCObject
{
public:
	Teleport(CCStringToStringDictionary *dict, int x, int y);
	~Teleport(void);
	//传送点所在位置
	CCPoint tileCoord;
	//传送到目标层后，勇士所在坐标
	CCPoint heroTileCoord;
	//目标地图的层数
	int targetMap;
	//唯一的ID
	int index;
	//图片纹理的文件路径
	CCString *imagePath;
	CCSprite *teleportSprite;
};
#endif