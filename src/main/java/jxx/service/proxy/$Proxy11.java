package jxx.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

import jxx.domain.User;
import jxx.service.IndexService;

public class $Proxy11 extends Proxy implements IndexService
{
	  private static Method m1;
	  private static Method m4;
	  private static Method m3;
	  private static Method m0;
	  private static Method m5;
	  private static Method m2;

	  public $Proxy11(InvocationHandler paramInvocationHandler)
	  {
	    super(paramInvocationHandler);
	  }

	  public final boolean equals(Object paramObject)
	  {
	    try
	    {
	      return ((Boolean)this.h.invoke(this, m1, new Object[] { paramObject })).booleanValue();
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      throw localRuntimeException;
	    }
	    catch (Throwable localThrowable)
	    {
		    throw new UndeclaredThrowableException(localThrowable);
	    }
	  }

	  public final User getUser()
	  {
	    try
	    {
	      return (User)this.h.invoke(this, m4, null);
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      throw localRuntimeException;
	    }
	    catch (Throwable localThrowable)
	    {
		    throw new UndeclaredThrowableException(localThrowable);
	    }
	  }

	  public final void writer(String paramString)
	  {
	    try
	    {
	      this.h.invoke(this, m3, new Object[] { paramString });
	      return;
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      throw localRuntimeException;
	    }
	    catch (Throwable localThrowable)
	    {
		    throw new UndeclaredThrowableException(localThrowable);
	    }
	  }

	  public final int hashCode()
	  {
	    try
	    {
	      return ((Integer)this.h.invoke(this, m0, null)).intValue();
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      throw localRuntimeException;
	    }
	    catch (Throwable localThrowable)
	    {
		    throw new UndeclaredThrowableException(localThrowable);
	    }
	  }

	  public final User getUserMore()
	  {
	    try
	    {
	      return (User)this.h.invoke(this, m5, null);
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      throw localRuntimeException;
	    }
	    catch (Throwable localThrowable)
	    {
		    throw new UndeclaredThrowableException(localThrowable);
	    }
	  }

	  public final String toString()
	  {
	    try
	    {
	      return (String)this.h.invoke(this, m2, null);
	    }
	    catch (RuntimeException localRuntimeException)
	    {
	      throw localRuntimeException;
	    }
	    catch (Throwable localThrowable)
	    {
		    throw new UndeclaredThrowableException(localThrowable);
	    }
	  }

	  static
	  {
	    try
	    {
	      m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] { Class.forName("java.lang.Object") });
	      m4 = Class.forName("jxx.service.IndexService").getMethod("getUser", new Class[0]);
	      m3 = Class.forName("jxx.service.IndexService").getMethod("writer", new Class[] { Class.forName("java.lang.String") });
	      m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
	      m5 = Class.forName("jxx.service.IndexService").getMethod("getUserMore", new Class[0]);
	      m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
	    }
	    catch (NoSuchMethodException localNoSuchMethodException)
	    {
	      throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
	    }
	    catch (ClassNotFoundException localClassNotFoundException)
	    {
		    throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
	    }
	  }
	}
