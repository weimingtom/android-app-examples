#ifndef __NPC_H__
#define __NPC_H__

#include "MTGame.h"

using namespace cocos2d;

class NPC : public CCObject
{
public:
	//构造函数中要根据传递的属性表初始化各个变量
	NPC(CCStringToStringDictionary *dict, int x, int y);
	~NPC(void);
	//用于显示npc的精灵
	CCSprite *npcSprite;
	//保存在TileMap中配置的name项
	CCString *npcId;
	//npc所在的TileMap坐标
	CCPoint tileCoord;
	//图片纹理的文件路径
	CCString *imagePath;
	//纹理的Rect
	CCRect rect;
	//对应配置中的type项
	CCString *type;
};

#endif