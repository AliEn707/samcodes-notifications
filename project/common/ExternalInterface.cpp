#ifndef STATIC_LINK
#define IMPLEMENT_API
#endif

#if defined(HX_WINDOWS) || defined(HX_MACOS) || defined(HX_LINUX)
#define NEKO_COMPATIBLE
#endif

#include <hx/CFFI.h>
#include "SamcodesNotifications.h"

using namespace samcodesnotifications;

#ifdef IPHONE
static value samcodesnotifications_schedule_local_notification(value slot, value triggerAfterMillis, value title, value message, value action, value incrementBadgeCount)
{
	scheduleLocalNotification(val_int(slot), val_int(triggerAfterMillis), val_string(title), val_string(message), val_string(action), val_bool(incrementBadgeCount));
	return alloc_null();
}
DEFINE_PRIM(samcodesnotifications_schedule_local_notification, 6);

static value samcodesnotifications_cancel_local_notification(value slot)
{
	cancelLocalNotification(val_int(slot));
	return alloc_null();
}
DEFINE_PRIM(samcodesnotifications_cancel_local_notification, 1);

static value samcodesnotifications_cancel_local_notifications()
{
	cancelLocalNotifications();
	return alloc_null();
}
DEFINE_PRIM(samcodesnotifications_cancel_local_notifications, 0);

static value samcodesnotifications_get_application_icon_badge_number()
{
	return alloc_int(getApplicationIconBadgeNumber());
}

static value samcodesnotifications_set_application_icon_badge_number(value number)
{
	return alloc_bool(setApplicationIconBadgeNumber(val_int(number)));
}

extern "C" void samcodesnotifications_main()
{
	val_int(0);
}
DEFINE_ENTRY_POINT(samcodesnotifications_main);

extern "C" int samcodesnotifications_register_prims()
{
	return 0;
}

#endif